package org.mbds.clients;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataType;
import org.mbds.clients.dto.ClientDto;
import org.mbds.clients.entities.ClientEntity;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.types.DataTypes.*;

public class Main {

    public interface ISparkAction {
        void handle(SparkSession spark);
    }

    static class ColumnDefinition{

        public String sourceName;
        public String finalName;
        public DataType type;

        public ColumnDefinition(String sourceName, String finalName, DataType type){
            this.sourceName = sourceName;
            this.finalName = finalName;
            this.type = type;
        }
    }

    private static String clientQuery = "select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid" + " " +
            "from mongodb.datalake.clients" + " " +
            "union distinct" + " " +
            "select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid" + " " +
            "from hive.datalake.clients";

    private static final String[][] collectionSexe = new String[][] {
            { "Femme", "F" },
            { "F", "F" },
            { "Féminin", "F" },
            { "Homme", "M" },
            { "M", "M" },
            { "Masculin", "M" }
    };

    private static final String[][] collectionSituation = new String[][] {
            { "En Couple", "Couple" },
            { "Divorcé", "Divorced" },
            { "Célibataire", "Single" },
            { "Seul", "Single" },
            { "Seule", "Single" },
            { "Marié", "Married" }
    };

    private static final Map<String, String> mapSexe = Stream.of(collectionSexe).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    private static final Map<String, String> mapSituation = Stream.of(collectionSituation).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    private static final Map<String, ISparkAction> mapAction = new HashMap<>();
    private static final List<ColumnDefinition> csvColumns = new ArrayList(Arrays.asList(
            new ColumnDefinition("age", "age", LongType),
            new ColumnDefinition("sexe", "sexe", StringType),
            new ColumnDefinition("taux", "taux", LongType),
            new ColumnDefinition("situationFamiliale", "situation", StringType),
            new ColumnDefinition("nbEnfantsAcharge", "nbchildren", LongType),
            new ColumnDefinition("2eme voiture", "havesecondcar", BooleanType),
            new ColumnDefinition("immatriculation", "registrationid", StringType)
    ));

    public static void main(String[] args) {

        mapAction.put("datalake", Main::datalakeTask);
        mapAction.put("dba", Main::dbaTask);

        SparkConf configuration = new SparkConf()
                .setAppName("Clients-job")
                .setMaster("spark://spark:7077")
                .set("spark.submit.deployMode", "client")
                .set("spark.ui.showConsoleProgress", "true")
                .set("spark.eventLog.enabled", "false")
                .set("spark.logConf", "false")
                .set("spark.driver.bindAddress", "0.0.0.0")
                .set("spark.driver.host", "spark");

        SparkSession spark = SparkSession
                .builder()
                .config(configuration)
                .getOrCreate();

        if(args.length > 0){
            ISparkAction sparkaction = mapAction.get(args[0]);
            if(sparkaction != null){
                sparkaction.handle(spark);
            }
        }

        spark.stop();
    }

    private static void dbaTask(SparkSession spark){

        Dataset<Row> dataset1 = spark.read()
                .format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .load("hdfs://namenode-dba:9000/user/hive/warehouse/dba.db/Clients_1.csv");

        Dataset<Row> dataset9 = spark.read()
                .format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .load("hdfs://namenode-dba:9000/user/hive/warehouse/dba.db/Clients_9.csv");

        Dataset<Row> dataset = dataset1.union(dataset9);

        for(ColumnDefinition definition : csvColumns)  {
            dataset = dataset.withColumnRenamed(definition.sourceName,definition.finalName);
            dataset = dataset.withColumn(definition.finalName, dataset.col(definition.finalName).cast(definition.type));
        }

        dataset.printSchema();
        dataset.show(false);

        JavaRDD<ClientDto> rdd = dataset
                .withColumn("id", monotonically_increasing_id())
                .as(Encoders.bean(ClientDto.class))
                .javaRDD();

        handleTask(spark, rdd, "jdbc:postgresql://postgres-data-dba:5432/postgres");
    }

    private static void datalakeTask(SparkSession spark){

        Dataset<ClientDto> dataset = spark.read()
                .format("jdbc")
                .option("url", "jdbc:presto://presto:8080")
                .option("query", clientQuery)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load()
                .withColumn("id", monotonically_increasing_id())
                .as(Encoders.bean(ClientDto.class));

        dataset.printSchema();
        dataset.show(false);

        handleTask(spark, dataset.javaRDD(), "jdbc:postgresql://postgres-data:5438/postgres");
    }

    private static void handleTask(SparkSession spark, JavaRDD<ClientDto> rdd, String urlPostgre){

        JavaRDD<ClientEntity> rddEntity = rdd.map(Main::mapClient);

        Dataset<Row> result = spark.createDataFrame(rddEntity, ClientEntity.class);
        result.show(false);

        result.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .format("jdbc")
                .option("url", urlPostgre)
                .option("dbtable", "datawarehouse.clients")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }

    private static ClientEntity mapClient(ClientDto client){

        ClientEntity entity = new ClientEntity();

        Integer age = longToInteger(client.getAge());
        String sexe = client.getSexe();
        Integer taux = longToInteger(client.getTaux());
        String situation = client.getSituation();
        Integer nbchildren = longToInteger(client.getNbchildren());
        Boolean havesecondcar = client.getHavesecondcar();
        String registrationid = client.getRegistrationid();

        age = age != null && age > 0 ? age : null;
        sexe = mapSexe.get(sexe);
        situation = mapSituation.get(situation);
        nbchildren = nbchildren != null && nbchildren >= 0 ? nbchildren : null;

        entity.setId(client.getId());
        entity.setAge(age);
        entity.setSexe(sexe);
        entity.setTaux(taux);
        entity.setSituation(situation);
        entity.setNbchildren(nbchildren);
        entity.setHavesecondcar(havesecondcar);
        entity.setRegistrationid(registrationid);

        return entity;
    }

    private static Integer longToInteger(Long value){
        if(value == null) return null;
        return Math.toIntExact(value);
    }
}
