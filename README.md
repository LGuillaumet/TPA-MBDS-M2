## TPA-MBDS-M2

# Contexte du Projet
Vous avez été contacté par un concessionnaire automobile afin de l'aider à mieux cibler les véhicules
susceptibles d'intéresser ses clients. Pour cela il met à votre disposition :
- Son catalogue de véhicules
- Son fichier clients concernant les achats de l'année en cours
- Un accès à toutes les informations sur les immatriculations effectuées cette année
- Une brève documentation des données
- Un vendeur (voir son interview ci-dessous)
# Votre client sera satisfait si vous lui proposez un moyen afin :
- Qu'un vendeur puisse en quelques secondes évaluer le type de véhicule le plus susceptible d'intéresser des clients qui se présentent dans la concession
- Qu'il puisse envoyer une documentation précise sur le véhicule le plus adéquat pour des clients sélectionnés par son service marketing (voir ci-dessous) 

# Setup

- Console App .NET 6
- SDK : https://dotnet.microsoft.com/en-us/download/dotnet/6.0
- Docker / Docker-compose : https://docs.docker.com/compose/install/

- Start docker services :
    - cd devops/containers
    - docker-compose build
    - docker-compose up
    - kafka ui : http://localhost:3040
    - presto ui : http://localhost:8080

- Injestion des données :
    - dezip fichier.zip
    - cd injestion/DataInjestion/DataInjestion/publish
    - edit appsettings.json => set path files and set isActive or not for upload
    - execute DataInjestion.exe

### Data injestion

Topics kafka :

- upserts-marketing-cassandra -> cassandra.datalake.marketing
- upserts-registration-cassandra -> cassandra.datalake.registration
- upserts-catalogue-hive -> hive.datalake.catalogue
- upserts-clients-hive -> hive.datalake.clients
- upserts-carbon-mongo -> mongodb.datalake.carbon
- upserts-clients-mongo -> mongodb.datalake.clients


### Datavis



### Data analyse 



