using Confluent.Kafka;
using DataInjestion.Settings;
using DataInjestion.Wrappers;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using System.Collections.Concurrent;
using System.Diagnostics;
using System.Text;

namespace DataInjestion.Services.Kafka
{

    public class KafkaProduceException : Exception
    {
        public KafkaProduceException()
        {
        }

        public KafkaProduceException(string message)
            : base(message)
        {
        }

        public KafkaProduceException(string message, Exception inner)
            : base(message, inner)
        {
        }
    }

    public class KafkaProducer
    {
        private readonly ILogger<KafkaProducer> Logger;
        private readonly IOptions<KafkaProducerConfiguration> Configuration;
        private readonly UpsertConfigurationWrapper UpsertWrapper;

        public KafkaProducer(IOptions<KafkaProducerConfiguration> configuration, UpsertConfigurationWrapper wrapper, ILogger<KafkaProducer> logger)
        {
            Logger = logger;
            Configuration = configuration;
            UpsertWrapper = wrapper;
        }

        public void BulkSendMessages(IList<string> datas)
        {
            if (UpsertWrapper.Configuration is null)
            {
                return;
            }

            var config = new ProducerConfig { 
                BootstrapServers = Configuration.Value.BrokerServer,
                //LingerMs = 200,
                //CompressionType = CompressionType.Snappy,
                //BatchSize = 16384 * 4
            };

            var message = new Message<string, string> { Key = string.Empty };

            using var producer = new ProducerBuilder<string, string>(config)
                .SetKeySerializer(Serializers.Utf8)
                .SetValueSerializer(Serializers.Utf8)
                .Build();

            try
            {
                var messages = datas.Select(data => new Message<string, string> { Key = string.Empty, Value = data }).ToList();
                ProduceBatch(producer, UpsertWrapper.Configuration.Topic, messages, TimeSpan.FromSeconds(10));
            }
            catch (Exception e)
            {
                Logger.LogError(e.Message);
            }
        }

        private void ProduceBatch<TKey, TVal>(IProducer<TKey, TVal> producer, string topic,
            IList<Message<TKey, TVal>> messages, TimeSpan flushTimeout, CancellationTokenSource cts = null)
        {
            var stopWatch = new Stopwatch();
            var errorReports = new ConcurrentQueue<DeliveryReport<TKey, TVal>>();
            var reportsExpected = 0;
            var reportsReceived = 0;
            const int flushWaitMs = 100;

            void DeliveryHandler(DeliveryReport<TKey, TVal> report)
            {
                Interlocked.Increment(ref reportsReceived);

                if (report.Error.IsError)
                {
                    errorReports.Enqueue(report);
                }
            }

            stopWatch.Start();
            for(int i = 0; i < messages.Count; i++)
            {
                try
                {
                    producer.Produce(topic, messages[i], DeliveryHandler);
                    reportsExpected++;
                }
                catch (ProduceException<string, string> e)
                {
                    if (e.Error == ErrorCode.Local_QueueFull)
                    {
                        producer.Flush(TimeSpan.FromMilliseconds(flushWaitMs));
                        Task.Delay(2000);
                        i--;
                    }
                    else
                    {
                        throw e;
                    }
                }
            }

            var deadline = DateTime.UtcNow + flushTimeout;
            
            while (DateTime.UtcNow < deadline && reportsReceived < reportsExpected)
            {
                cts?.Token.ThrowIfCancellationRequested();
                producer.Flush(TimeSpan.FromMilliseconds(flushWaitMs));
            }

            if (!errorReports.IsEmpty)
            {
                throw new AggregateException($"{errorReports.Count} Kafka produce(s) failed. Up to 10 inner exceptions follow.",
                    errorReports.Take(10).Select(i => new KafkaProduceException(
                        $"A Kafka produce error occurred. Topic: {topic}, Message key: {i.Message.Key}, Code: {i.Error.Code}, Reason: " +
                        $"{i.Error.Reason}, IsBroker: {i.Error.IsBrokerError}, IsLocal: {i.Error.IsLocalError}, IsFatal: {i.Error.IsFatal}"
                    ))
                );
            }

            if (reportsReceived < reportsExpected)
            {
                var msg = $"Kafka producer flush did not complete within the timeout; only received {reportsReceived} " +
                          $"delivery reports out of {reportsExpected} expected.";
                throw new KafkaProduceException(msg);
            }

            stopWatch.Stop();
            PrintMessageRate(stopWatch, reportsReceived);
        }

        private void PrintMessageRate(Stopwatch stopwatch, int reportsReceived)
        {
            var second = stopwatch.ElapsedMilliseconds / 1000;
            if (second > 0)
            {
                var rate = second == 0 ? 0 : reportsReceived / second;
                Logger.LogInformation($"Message sending rate : { rate } m/s");
            }
        }
    }
}
