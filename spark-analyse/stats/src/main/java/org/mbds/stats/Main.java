package org.mbds.stats;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.mbds.stats.dto.MarqueClientsDto;
import org.mbds.stats.entities.StatAgeMarque;
import org.mbds.stats.entities.StatNbchildrenMarque;
import org.mbds.stats.entities.StatTauxMarque;
import org.mbds.stats.entities.StatTotalMarque;
import org.mbds.stats.models.MarqueKey;
import scala.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static String query = "select r.marque, c.age, c.taux, c.nbchildren " +
            "from " +
            "( " +
            "select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid " +
            "from mongodb.datalake.clients " +
            "union all " +
            "select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid " +
            "from hive.datalake.clients " +
            ") c " +
            "inner join ( " +
            "select registrationid, marque " +
            "from cassandra.datalake.registration " +
            ") r on r.registrationid = c.registrationid";

    private static final String[][] collectionMarque = new String[][] {
            { "Hyundaè", "Hyundai" }
    };

    private static final Map<String, String> mapMarque = Stream.of(collectionMarque).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static void main(String[] args) {

        SparkSession spark = getSession();

        JavaRDD<MarqueClientsDto> rdd = spark.read()
                .format("jdbc")
                .option("url", "jdbc:presto://presto:8080")
                .option("query", query)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load()
                .as(Encoders.bean(MarqueClientsDto.class)).toJavaRDD();

        rdd = rdd.map(element -> {
            String base = element.getMarque();
            String marque = mapMarque.get(base);
            marque = (marque != null ? marque : base).toUpperCase();
            element.setMarque(marque);
            return element;
        }).filter(element -> element.getAge() > 0 && element.getTaux() >= 0 && element.getNbchildren() >= 0);

        statTotalOnAllMarque(rdd, spark);
        statAgeOnAllMarque(rdd, spark);
        statTauxOnAllMarque(rdd, spark);
        statNbchildrenOnAllMarque(rdd, spark);

        spark.stop();
    }

    private static void statTotalOnAllMarque(JavaRDD<MarqueClientsDto> rdd, SparkSession spark){

        Map<String, List<MarqueClientsDto>> rddMarque = groupByCriteria(rdd, element -> Tuple2.apply(element.getMarque(), element)).collectAsMap();

        List<StatTotalMarque> results = new ArrayList<>();
        for (Map.Entry<String,List<MarqueClientsDto>> entry : rddMarque.entrySet()){

            Dataset<MarqueClientsDto> result = spark.createDataFrame(entry.getValue(), MarqueClientsDto.class)
                    .as(Encoders.bean(MarqueClientsDto.class));

            double[][] appro = result.stat().approxQuantile(new String[] { "age", "taux", "nbchildren" }, new double[] { 0.0, 0.25, 0.5, 0.75, 1}, 0.0);
            StatTotalMarque total = new StatTotalMarque();
            total.setMarque(entry.getKey());

            total.setQ0age(appro[0][0]);
            total.setQ1age(appro[0][1]);
            total.setQ2age(appro[0][2]);
            total.setQ3age(appro[0][3]);
            total.setQ4age(appro[0][4]);

            total.setQ0taux(appro[1][0]);
            total.setQ1taux(appro[1][1]);
            total.setQ2taux(appro[1][2]);
            total.setQ3taux(appro[1][3]);
            total.setQ4taux(appro[1][4]);

            total.setQ0nbchildren(appro[2][0]);
            total.setQ1nbchildren(appro[2][1]);
            total.setQ2nbchildren(appro[2][2]);
            total.setQ3nbchildren(appro[2][3]);
            total.setQ4nbchildren(appro[2][4]);

            results.add(total);
        }

        Dataset<StatTotalMarque> rows = spark.createDataFrame(results, StatTotalMarque.class)
                .as(Encoders.bean(StatTotalMarque.class));

        rows.printSchema();
        rows.show();

        rows.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .option("cascadeTruncate", true)
                .format("jdbc")
                .option("url", "jdbc:postgresql://postgres-data:5432/postgres")
                .option("dbtable", "datawarehouse.carmarque_total_stats")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();

    }

    private static void statAgeOnAllMarque(JavaRDD<MarqueClientsDto> rdd, SparkSession spark){

        Map<MarqueKey, List<MarqueClientsDto>> rddMarque = groupByCriteria(rdd, value -> Tuple2.apply(new MarqueKey(value.getMarque(), value.getAge()), value)).collectAsMap();

        List<StatAgeMarque> results = new ArrayList<>();
        for (Map.Entry<MarqueKey,List<MarqueClientsDto>> entry : rddMarque.entrySet()){

            Dataset<MarqueClientsDto> result = spark.createDataFrame(entry.getValue(), MarqueClientsDto.class)
                    .as(Encoders.bean(MarqueClientsDto.class));

            double[][] appro = result.stat().approxQuantile(new String[] { "taux", "nbchildren" }, new double[] { 0.0, 0.25, 0.5, 0.75, 1}, 0.0);

            StatAgeMarque total = new StatAgeMarque();

            total.setMarque(entry.getKey().getMarque());
            total.setAge(entry.getKey().getValue());

            total.setQ0taux(appro[0][0]);
            total.setQ1taux(appro[0][1]);
            total.setQ2taux(appro[0][2]);
            total.setQ3taux(appro[0][3]);
            total.setQ4taux(appro[0][4]);

            total.setQ0nbchildren(appro[1][0]);
            total.setQ1nbchildren(appro[1][1]);
            total.setQ2nbchildren(appro[1][2]);
            total.setQ3nbchildren(appro[1][3]);
            total.setQ4nbchildren(appro[1][4]);

            results.add(total);
        }

        Dataset<StatAgeMarque> rows = spark.createDataFrame(results, StatAgeMarque.class)
                .as(Encoders.bean(StatAgeMarque.class));

        rows.printSchema();
        rows.show();

        rows.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .option("cascadeTruncate", true)
                .format("jdbc")
                .option("url", "jdbc:postgresql://postgres-data:5432/postgres")
                .option("dbtable", "datawarehouse.carmarque_age_stats")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }

    private static void statTauxOnAllMarque(JavaRDD<MarqueClientsDto> rdd, SparkSession spark){

        Map<MarqueKey, List<MarqueClientsDto>> rddMarque = groupByCriteria(rdd, value -> Tuple2.apply(new MarqueKey(value.getMarque(), value.getTaux()), value)).collectAsMap();

        List<StatTauxMarque> results = new ArrayList<>();
        for (Map.Entry<MarqueKey,List<MarqueClientsDto>> entry : rddMarque.entrySet()){

            Dataset<MarqueClientsDto> result = spark.createDataFrame(entry.getValue(), MarqueClientsDto.class)
                    .as(Encoders.bean(MarqueClientsDto.class));

            double[][] appro = result.stat().approxQuantile(new String[] { "age", "nbchildren" }, new double[] { 0.0, 0.25, 0.5, 0.75, 1}, 0.0);

            StatTauxMarque total = new StatTauxMarque();

            total.setMarque(entry.getKey().getMarque());
            total.setTaux(entry.getKey().getValue());

            total.setQ0age(appro[0][0]);
            total.setQ1age(appro[0][1]);
            total.setQ2age(appro[0][2]);
            total.setQ3age(appro[0][3]);
            total.setQ4age(appro[0][4]);

            total.setQ0nbchildren(appro[1][0]);
            total.setQ1nbchildren(appro[1][1]);
            total.setQ2nbchildren(appro[1][2]);
            total.setQ3nbchildren(appro[1][3]);
            total.setQ4nbchildren(appro[1][4]);

            results.add(total);
        }

        Dataset<StatTauxMarque> rows = spark.createDataFrame(results, StatTauxMarque.class)
                .as(Encoders.bean(StatTauxMarque.class));

        rows.printSchema();
        rows.show();

        rows.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .option("cascadeTruncate", true)
                .format("jdbc")
                .option("url", "jdbc:postgresql://postgres-data:5432/postgres")
                .option("dbtable", "datawarehouse.carmarque_taux_stats")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }

    private static void statNbchildrenOnAllMarque(JavaRDD<MarqueClientsDto> rdd, SparkSession spark){

        Map<MarqueKey, List<MarqueClientsDto>> rddMarque = groupByCriteria(rdd, value -> Tuple2.apply(new MarqueKey(value.getMarque(), value.getNbchildren()), value)).collectAsMap();

        List<StatNbchildrenMarque> results = new ArrayList<>();
        for (Map.Entry<MarqueKey,List<MarqueClientsDto>> entry : rddMarque.entrySet()){

            Dataset<MarqueClientsDto> result = spark.createDataFrame(entry.getValue(), MarqueClientsDto.class)
                    .as(Encoders.bean(MarqueClientsDto.class));

            double[][] appro = result.stat().approxQuantile(new String[] { "age", "taux" }, new double[] { 0.0, 0.25, 0.5, 0.75, 1}, 0.0);
            StatNbchildrenMarque total = new StatNbchildrenMarque();

            total.setMarque(entry.getKey().getMarque());
            total.setNbchildren(entry.getKey().getValue());

            total.setQ0age(appro[0][0]);
            total.setQ1age(appro[0][1]);
            total.setQ2age(appro[0][2]);
            total.setQ3age(appro[0][3]);
            total.setQ4age(appro[0][4]);

            total.setQ0taux(appro[1][0]);
            total.setQ1taux(appro[1][1]);
            total.setQ2taux(appro[1][2]);
            total.setQ3taux(appro[1][3]);
            total.setQ4taux(appro[1][4]);

            results.add(total);
        }

        Dataset<StatNbchildrenMarque> rows = spark.createDataFrame(results, StatNbchildrenMarque.class)
                .as(Encoders.bean(StatNbchildrenMarque.class));

        rows.printSchema();
        rows.show();

        rows.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .option("cascadeTruncate", true)
                .format("jdbc")
                .option("url", "jdbc:postgresql://postgres-data:5432/postgres")
                .option("dbtable", "datawarehouse.carmarque_nbchildren_stats")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }

    private static <T,K2,V2> JavaPairRDD<K2, List<V2>> groupByCriteria(JavaRDD<T> rdd, PairFunction<T, K2, V2> f){
        return rdd.mapToPair(f)
                .aggregateByKey(
                        new ArrayList<>(),
                        (v1, v2) -> { v1.add(v2); return v1; },
                        (v1, v2) -> { v1.addAll(v2); return v1; }
                );
    }

    private static SparkSession getSession(){
        SparkConf configuration = new SparkConf()
                .setAppName("Cars-job")
                .setMaster("spark://spark:7077")
                .set("spark.submit.deployMode", "client")
                .set("spark.ui.showConsoleProgress", "true")
                .set("spark.eventLog.enabled", "false")
                .set("spark.logConf", "false")
                .set("spark.driver.bindAddress", "0.0.0.0")
                .set("spark.driver.host", "spark");

        return SparkSession.builder().config(configuration).getOrCreate();
    }


}

