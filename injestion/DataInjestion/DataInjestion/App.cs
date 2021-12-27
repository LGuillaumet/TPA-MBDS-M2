using ChoETL;
using DataInjestion.Helpers;
using DataInjestion.Mappers;
using DataInjestion.Services.Kafka;
using DataInjestion.Settings;
using DataInjestion.Wrappers;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Serilog.Context;
using Serilog.Enrichers;
using System.Data;
using System.Text;
using Utf8Json;

namespace DataInjestion
{
    public class App
    {
        private readonly IServiceProvider ServiceProvider;
        private readonly IOptions<UpsertConfigurations> Configuration; 
        private readonly ILogger<App> Logger;

        public App(IServiceProvider serviceProvider, IOptions<UpsertConfigurations> configuration, ILogger<App> logger)
        {
            ServiceProvider = serviceProvider;
            Configuration = configuration;
            Logger = logger;
        }

        public Task ExecuteAsync()
        {
            Logger.LogInformation("Start application execution. Creating all tasks.");

            var tasks = Configuration.Value.Configurations
                .Where(conf => conf.IsActive)
                .Select(conf => Task.Run(() => {
                    LogContext.PushProperty(ThreadNameEnricher.ThreadNamePropertyName, conf.Topic);
                    ExecuteTask(ServiceProvider.CreateScope(), conf);
                }));

            Logger.LogInformation("Waiting all tasks completion.");

            return Task.WhenAll(tasks);
        }

        public void ExecuteTask(IServiceScope serviceScope, UpsertConfiguration configuration)
        {
            Logger.LogInformation($"Starting parse and serialize file : {configuration.FilePathSource}.");

            var list = CsvMapper.Map(configuration);

            foreach (var field in configuration.AdditionalJsonFields)
            {
                foreach(var element in list)
                {
                    element.Add(field.Name, TypesHelper.Types[field.Type].Generate());
                }
            }

            var result = list.Select(element => Encoding.UTF8.GetString(JsonSerializer.Serialize(element))).ToList();

            Logger.LogInformation($"Complete parse and serialize : {configuration.FilePathSource}.");

            var serviceProvider = serviceScope.ServiceProvider;

            var wrapper = serviceProvider.GetRequiredService<UpsertConfigurationWrapper>();
            wrapper.Configuration = configuration;

            var producer = serviceProvider.GetRequiredService<KafkaProducer>();

            Logger.LogInformation($"Starting produce to : {configuration.Topic}.");

            producer.BulkSendMessages(result);

            Logger.LogInformation($"Complete produce to : {configuration.Topic}.");
        }
    }
}
