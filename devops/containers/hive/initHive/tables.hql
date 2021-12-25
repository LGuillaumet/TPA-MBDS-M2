create database if not exists datalake;
use datalake;

CREATE TABLE if not exists clients (
  age bigint,
  sexe string,
  taux bigint,
  situation string,
  nbChildren bigint,
  haveSecondCar boolean,
  immatriculation string
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe'
STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'
LOCATION 'hdfs://namenode:9000/user/hive/warehouse/datalake.db/clients';

create external table if not exists catalogue (
  marque string,
  nom string,
  puissance bigint,
  longueur string,
  nbPlaces bigint,
  nbPortes bigint,
  couleur string,
  occasion boolean,
  prix double
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe'
STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'
LOCATION 'hdfs://namenode:9000/user/hive/warehouse/datalake.db/catalogue';