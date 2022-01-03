using ChoETL;
using DataInjestion.Helpers;
using DataInjestion.Settings;

namespace DataInjestion.Mappers
{
    public static class CsvMapper
    {

        public static IList<IDictionary<string, object>> Map(UpsertConfiguration configuration)
        {
            List<IDictionary<string, object>> list = new();
            ChoCSVRecordConfiguration config = new();

            config.FileHeaderConfiguration.HasHeaderRecord = configuration.FileHaveHeader;
            config.FileHeaderConfiguration.IgnoreHeader = true;
            config.Delimiter = configuration.FieldSeparator;
            config.Encoding = System.Text.Encoding.UTF8;
            config.QuoteAllFields = true;
            config.IgnoreFieldValueMode = ChoIgnoreFieldValueMode.WhiteSpace;

             foreach (var schema in configuration.JsonSchemas)
             {
                 var t = TypesHelper.Types[schema.Type];
                 config.CSVRecordFieldConfigurations.Add(new ChoCSVRecordFieldConfiguration(schema.Name, schema.Position)
                 {
                     FieldType = t.Type,
                     DefaultValue = t.Default
                 });
             }

            dynamic row = null;
            using (var parser = new ChoCSVReader(configuration.FilePathSource, config))
            {
                while ((row = parser.Read()) != null)
                {
                    list.Add(row);
                }
            }

            return list;
        }
    }
}
