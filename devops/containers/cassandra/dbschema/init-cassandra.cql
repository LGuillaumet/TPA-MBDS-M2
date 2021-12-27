CREATE KEYSPACE IF NOT EXISTS datalake WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};
USE datalake;

CREATE TABLE IF NOT EXISTS registration (
   registrationId text PRIMARY KEY,
   marque text,
   nom text,
   puissance int,
   longueur text,
   nbPlaces int, 
   nbPortes int,
   couleur text,
   occasion boolean,
   prix decimal 
);

CREATE TABLE IF NOT EXISTS marketing (
   id text PRIMARY KEY,
   age int,
   sexe text,
   taux int,
   situation text,
   nbChildren int,
   haveSecondCar boolean
);