using DataInjestion.Services.Kafka;
using DataInjestion.Settings;
using DataInjestion.Wrappers;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Serilog;

namespace DataInjestion
{
    public class Startup
    {
        public static void BuildConfig(IConfigurationBuilder builder)
        {
            // Check the current directory that the application is running on 
            // Then once the file 'appsetting.json' is found, we are adding it.
            // We add env variables, which can override the configs in appsettings.json
            builder.SetBasePath(Directory.GetParent(AppContext.BaseDirectory)?.FullName)
                .AddJsonFile("appsettings.json", false, true)
                .AddEnvironmentVariables();
        }

        public static void ConfigureServices(IServiceCollection serviceCollection, IConfigurationRoot configuration)
        {
  
            serviceCollection.Configure<UpsertConfigurations>(configuration.GetSection(nameof(UpsertConfigurations)));
            serviceCollection.Configure<KafkaProducerConfiguration>(configuration.GetSection(nameof(KafkaProducerConfiguration)));

            // Add app
            serviceCollection.AddTransient<App>();
            serviceCollection.AddScoped<KafkaProducer>();
            serviceCollection.AddScoped<UpsertConfigurationWrapper>();
        }
    }
}
