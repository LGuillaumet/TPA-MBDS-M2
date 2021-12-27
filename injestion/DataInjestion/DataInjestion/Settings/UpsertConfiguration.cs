
namespace DataInjestion.Settings
{
    public class UpsertConfiguration
    {
        public bool IsActive { get; set; }
        public string Topic { get; set; } = string.Empty;
        public string FilePathSource { get; set; } = string.Empty;
        public bool FileHaveHeader { get; set; }
        public string FieldSeparator { get; set; } = string.Empty;
        public List<AdditionalJsonField> AdditionalJsonFields { get; set; } = new List<AdditionalJsonField>();
        public List<JsonSchema> JsonSchemas { get; set; } = new List<JsonSchema>();
    }
}
