# Devops

- Docker / Docker-compose : https://docs.docker.com/compose/install/

## Setup

- Start docker services :
    - cd containers
    - docker-compose build
    - docker-compose up
    - kafka ui : http://localhost:3040
    - presto ui : http://localhost:8080

## Kafka 

https://fr.wikipedia.org/wiki/Apache_Kafka

Apache Kafka est un projet à code source ouvert d'agent de messages développé par l'Apache Software Foundation et écrit en Scala. Le projet vise à fournir un système unifié, en temps réel à latence faible pour la manipulation de flux de données.

Image docker : landoop/fast-data-dev:latest

Nous permet d'ingérer les données dans les différentes base de données du datalake.

Website : http://localhost:3040

### Ports 

- 9092 : broker de messages
- 3040 : website / monitoring
- 7083 : kafka connect api

### CMD

- bash : docker exec -it kafka /bin/bash
- confluent : cd /opt/landoop/kafka/bin

### Clients 

- .Net : https://github.com/confluentinc/confluent-kafka-dotnet

## Databases

- Mongodb port 27017
- Cassandra port 9042
    - docker exec -it cassandra /bin/bash
    - cqlsh -u cassandra -p cassandra
- Hive
    - docker exec -it hive-server /bin/bash

## Presto 

https://fr.wikipedia.org/wiki/Presto_(moteur_de_requ%C3%AAte_SQL)

Presto est un projet distribué sous licence Apache, qui consiste en un moteur de requête SQL optimisé pour les interactions temps réel.

Permet le requêtage et jointure de données entre toutes les databases. 

Documentation : https://prestodb.io/docs/current/
Ressources : https://prestodb.io/resources.html

### CMD

- bash : docker exec -it presto /bin/bash
- cli : docker exec -it presto  presto-cli

### Liste des tables accessibles

- mongodb.datalake
    - clients
    - carbon

- cassandra.datalake
    - registration
    - marketing

- hive.datalake
    - clients
    - catalogue

Example : 

- docker exec -it presto  presto-cli
- select * from mongodb.datalake.clients;
- select * from mongodb.datalake.clients m left join cassandra.datalake.registration c on m.immatriculation = c.id;

### Clients 

- R: https://github.com/prestodb/RPresto
- Java : https://prestodb.io/docs/current/installation/jdbc.html 
- Node : https://github.com/tagomoris/presto-client-node
- .NET : https://github.com/bamcis-io/PrestoClient



