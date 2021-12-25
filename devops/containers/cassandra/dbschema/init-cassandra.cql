CREATE KEYSPACE IF NOT EXISTS datalake WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};
USE datalake;

CREATE TABLE IF NOT EXISTS registration (
   id text PRIMARY KEY,
   marque text,
   nom text,
   puissance varint,
   longueur text,
   nbPlaces varint, 
   nbPortes varint,
   couleur text,
   occasion boolean,
   prix decimal 
);

CREATE TABLE IF NOT EXISTS marketing (
   id varint PRIMARY KEY,
   age varint,
   sexe text,
   taux varint,
   situation text,
   nbChildren varint,
   haveSecondCar boolean
);