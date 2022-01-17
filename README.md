## TPA-MBDS-M2

# Vidéo explication
https://youtu.be/ByqajUCg2uE


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
- Java 8
- Node / npm

### Data injestion

Topics kafka :

- upserts-marketing-cassandra -> cassandra.datalake.marketing
- upserts-registration-cassandra -> cassandra.datalake.registration
- upserts-catalogue-hive -> hive.datalake.catalogue
- upserts-clients-hive -> hive.datalake.clients
- upserts-carbon-mongo -> mongodb.datalake.carbon
- upserts-clients-mongo -> mongodb.datalake.clients

### Datavis

- UI React : ./d3js-app
- API Node : ./d3js-api

### Data analyse 

L'analyse est disponible à travers différents dossiers :

- Spark Netoyage des données et transformations : ./spark-analyse
- R clustering et prédiction : ./r_analyse
- Python IA prédiction : ./python-analyse

### Procédure

#### Lancement du datalake 

Création des images et aux démarrage des containers créer les différentes tables du datalake dans chaque base, et les connecteurs/topics kafka.

- cd ./devops/datalake
- docker-compose build
- docker-compose up

Kafka UI : http://localhost:3040
PrestoDB UI : http://localhost:8080

#### Injestion des données 

Il est nécessaire au préalable de dezip les fichiers CSV du projet 

- cd ./injestion/DataInjection/release-injestion
- Modifier le fichier appsettings.json pour localiser chaque fichiers CSV et les liés à un topic kafka
- DataInjestion.exe pour lancer l'injestion 

#### Netoyage et transformation des données du datalake vers postgres

Traitement avec le container Spark

- docker exec -it spark /bin/bach
- ./bin/spark-submit --class org.mbds.cars.Main /spark_jobs/cars-1-jar-with-dependencies.jar
- ./bin/spark-submit --class org.mbds.clients.Main /spark_jobs/clients-1-jar-with-dependencies.jar
- ./bin/spark-submit --class org.mbds.marketing.Main /spark_jobs/marketing-1-jar-with-dependencies.jar
- ./bin/spark-submit --class org.mbds.co2.Main /spark_jobs/co2-1.jar
- ./bin/spark-submit --class org.mbds.stats.Main /spark_jobs/stats-1.jar

#### Clustering et prédiction 

Executer les scripts R : 

- ./r_analyse/datalake/cars-clustering.R
- ./r_analyse/datalake/clients-clustering.R

#### Prédiction IA

Executer :

- python ./python-analyse/model-training.py
- python ./python-analyse/marketing-prediction.py

### Visualisation 

Start API :

- cd ./d3js-api
- npm i
- node index.js

Start UI :

- cd ./d3js-app
- npm install --global yarn
- yarn start
