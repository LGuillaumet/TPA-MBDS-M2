# Data injestion

- Console App .NET 6
- SDK : https://dotnet.microsoft.com/en-us/download/dotnet/6.0

## Setup

Il est nécessaire au préalable de dezip les fichiers CSV du projet et d'avoir le datalake de lancer

- cd ./injestion/DataInjection/release-injestion
- Modifier le fichier appsettings.json pour localiser chaque fichiers CSV et les liés à un topic kafka
- DataInjestion.exe pour lancer l'injestion 