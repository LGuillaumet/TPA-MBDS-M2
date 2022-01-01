package org.mbds.co2;

import org.mbds.co2.dto.Co2Dto;
import org.mbds.co2.entities.Co2Entity;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;

import static org.apache.spark.sql.functions.monotonically_increasing_id;

public class Main {

    private static String CO2Query = "select marquemodel, bonusmalus, rejection, energiecost" + " " +
            "from mongodb.datalake.carbon";


    public static void main(String[] args) {
        SparkConf configuration = new SparkConf()
                .setAppName("CO2-job")
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


        Dataset<Co2Dto> dataset = spark.read()
                .format("jdbc")
                .option("url", "jdbc:presto://presto:8080")
                .option("query", CO2Query)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load()
                .withColumn("id", monotonically_increasing_id())
                .as(Encoders.bean(Co2Dto.class));

        dataset.printSchema();
        dataset.show(false);

        JavaRDD<Co2Dto> rdd = dataset.javaRDD();

        JavaRDD<Co2Entity> rddEntity = rdd.map(Main::mapCo2);

        Dataset<Row> result = spark.createDataFrame(rddEntity, Co2Entity.class);
        result.show(false);

        result.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .format("jdbc")
                .option("url", "jdbc:postgresql://postgres-data:5438/postgres")
                .option("dbtable", "datawarehouse.co2")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();

        spark.stop();
    }

    private static Co2Entity mapCo2(Co2Dto co2){

        Co2Entity entity = new Co2Entity();

        String marquemodel = co2.getMarquemodel().trim().split(" ")[0].replace("\"","");
        Integer bonusmalus = Integer.valueOf(co2.getBonusmalus().trim().replace(" ","").split("€")[0]);
        Integer rejection = Integer.valueOf(co2.getRejection().contains("-") ? null : co2.getRejection());
        Integer energiecost = Integer.valueOf(co2.getEnergiecost().trim().replace(" ","").split("€")[0]);

        entity.setId(co2.getId());
        entity.setMarquemodel(marquemodel);
        entity.setBonusmalus(bonusmalus);
        entity.setRejection(rejection);
        entity.setEnergiecost(energiecost);

        return entity;
    }
}
