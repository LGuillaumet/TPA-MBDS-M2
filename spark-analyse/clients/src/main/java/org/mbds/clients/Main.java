package org.mbds.clients;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.mbds.clients.dto.ClientDto;
import org.mbds.clients.entities.ClientEntity;
import scala.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.spark.sql.functions.monotonically_increasing_id;

/***
 * https://github.com/spirom/learning-spark-with-java/blob/master/src/main/java/rdd/Basic.java
 */
public class Main {

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

    public static void main(String[] args) {
        //
        // The "modern" way to initialize Spark is to create a SparkSession
        // although they really come from the world of Spark SQL, and Dataset
        // and DataFrame.
        //

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

        JavaRDD<ClientDto> rdd = dataset.javaRDD();

        JavaRDD<ClientEntity> rddEntity = rdd.map(Main::mapClient);

        Dataset<Row> result = spark.createDataFrame(rddEntity, ClientEntity.class);
        result.show(false);

        result.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .format("jdbc")
                .option("url", "jdbc:postgresql://postgres-data:5438/postgres")
                .option("dbtable", "datawarehouse.clients")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();

        spark.stop();
    }

    private static ClientEntity mapClient(ClientDto client){

        ClientEntity entity = new ClientEntity();

        Integer age = Math.toIntExact(client.getAge());
        String sexe = client.getSexe();
        Integer taux = Math.toIntExact(client.getTaux());
        String situation = client.getSituation();
        Integer nbchildren = Math.toIntExact(client.getNbchildren());
        boolean havesecondcar = client.isHavesecondcar();
        String registrationid = client.getRegistrationid();

        age = age <= 0 ? null : age;
        sexe = mapSexe.get(sexe);
        situation = mapSituation.get(situation);
        nbchildren = nbchildren >= 0 ? nbchildren : null;

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
}
