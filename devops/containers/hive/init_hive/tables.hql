create database if not exists datalake;
use datalake;

CREATE external TABLE if not exists clients (
  age bigint,
  sexe string,
  taux bigint,
  situation string,
  nbChildren bigint,
  haveSecondCar boolean,
  registrationId string
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe'
STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'
LOCATION 'hdfs://namenode:9000/user/hive/warehouse/datalake.db/clients';

DROP INDEX IF EXISTS index_clients_registration ON clients;
CREATE INDEX index_clients_registration ON TABLE clients(registrationId) AS 'org.apache.hadoop.hive.ql.index.compact.CompactIndexHandler' WITH DEFERRED REBUILD ; 

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

DROP INDEX IF EXISTS index_catalogue_marque ON clients;
DROP INDEX IF EXISTS index_catalogue_nom ON clients;
DROP INDEX IF EXISTS index_catalogue_marque_nom ON clients;
CREATE INDEX index_catalogue_marque ON TABLE catalogue(marque) AS 'org.apache.hadoop.hive.ql.index.compact.CompactIndexHandler' WITH DEFERRED REBUILD ; 
CREATE INDEX index_catalogue_nom ON TABLE catalogue(nom) AS 'org.apache.hadoop.hive.ql.index.compact.CompactIndexHandler' WITH DEFERRED REBUILD ; 
CREATE INDEX index_catalogue_marque_nom ON TABLE catalogue(marque,nom) AS 'org.apache.hadoop.hive.ql.index.compact.CompactIndexHandler' WITH DEFERRED REBUILD ; 