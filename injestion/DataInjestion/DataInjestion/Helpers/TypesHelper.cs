

namespace DataInjestion.Helpers
{
    public static class TypesHelper
    {
        public static readonly Dictionary<string, (Type Type, object? Default, Func<object> Generate)> Types = new()
        {
            { "string", ( typeof(string), default(string), new Func<object>(() => string.Empty)) },
            { "bool", (typeof(bool), default(bool), new Func<object>(() => default(bool))) },
            { "boolean", (typeof(bool), default(bool), new Func<object>(() => default(bool))) },
            { "int", (typeof(int), default(int), new Func<object>(() => default(int))) },
            { "int64", (typeof(Int64), default(Int64), new Func<object>(() => default(Int64))) },
            { "int32", (typeof(int), default(int), new Func<object>(() => default(int))) },
            { "decimal", (typeof(decimal), default(decimal), new Func<object>(() => default(decimal))) },
            { "long", (typeof(long), default(long), new Func<object>(() => default(long))) },
            { "double", (typeof(double), default(double), new Func<object>(() => default(double))) },
            { "float", (typeof(float), default(float), new Func<object>(() => default(float))) },
            { "guid", (typeof(string), default(string), new Func<object>(() => Guid.NewGuid().ToString())) }
        };
    }
}
