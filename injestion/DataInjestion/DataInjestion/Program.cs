
using DataInjestion;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Serilog;

try
{

    var builder = new ConfigurationBuilder();
    Startup.BuildConfig(builder);
    var configuration = builder.Build();

    Log.Logger = new LoggerConfiguration()
        .ReadFrom.Configuration(configuration)
        .WriteTo.Console(Serilog.Events.LogEventLevel.Debug, outputTemplate: "[{Timestamp:yyyy-MM-dd HH:mm:ss.fff zzz} {Level:u3}] {Message:lj} <{ThreadId}><{ThreadName}> {NewLine}{Exception}")
        .MinimumLevel.Debug()
        .Enrich.FromLogContext()
        .Enrich.WithThreadId()
        .CreateLogger();

    Log.Logger.Information("Application Starting");

    var host = Host.CreateDefaultBuilder() 
        .ConfigureServices((context, services) => Startup.ConfigureServices(services, configuration))
        .UseSerilog()
        .Build();

    var app = host.Services.GetRequiredService<App>();
    await app.ExecuteAsync();

}
catch(Exception e)
{
    Log.Error(e.Message);
    Environment.Exit(1);
}

Environment.Exit(0);