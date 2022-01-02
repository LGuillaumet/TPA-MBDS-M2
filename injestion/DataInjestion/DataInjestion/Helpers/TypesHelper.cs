

namespace DataInjestion.Helpers
{
    public static class TypesHelper
    {
        public static readonly Dictionary<string, (Type Type, object? Default, Func<object> Generate)> Types = new()
        {
            { "string", ( typeof(string), default(string), new Func<object>(() => string.Empty)) },
            { "bool", (typeof(bool), null, new Func<object>(() => default(bool))) },
            { "boolean", (typeof(bool), null, new Func<object>(() => default(bool))) },
            { "int", (typeof(int), null, new Func<object>(() => default(int))) },
            { "int64", (typeof(Int64), null, new Func<object>(() => default(Int64))) },
            { "int32", (typeof(int), null, new Func<object>(() => default(int))) },
            { "decimal", (typeof(decimal), null, new Func<object>(() => default(decimal))) },
            { "long", (typeof(long), null, new Func<object>(() => default(long))) },
            { "double", (typeof(double), null, new Func<object>(() => default(double))) },
            { "float", (typeof(float), null, new Func<object>(() => default(float))) },
            { "guid", (typeof(string), null, new Func<object>(() => Guid.NewGuid().ToString())) }
        };
    }
}
