package org.mbds.co2;

import org.apache.spark.sql.types.DataType;
import org.mbds.co2.dto.Co2Dto;
import org.mbds.co2.entities.Co2Entity;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;

import java.util.*;

import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.types.DataTypes.StringType;

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

    private static final Map<String, ISparkAction> mapAction = new HashMap<>();

    private static String CO2Query = "select marquemodel, bonusmalus, rejection, energiecost" + " " +
            "from mongodb.datalake.carbon";

    private static final List<ColumnDefinition> csvColumns = new ArrayList(Arrays.asList(
            new ColumnDefinition("Marque / Modele", "marquemodel", StringType),
            new ColumnDefinition("Bonus / Malus", "bonusmalus", StringType),
            new ColumnDefinition("Rejets CO2 g/km", "rejection", StringType),
            new ColumnDefinition("Cout enerie", "energiecost", StringType)
    ));

    public static void main(String[] args) {

        mapAction.put("datalake", Main::datalakeTask);
        mapAction.put("dba", Main::dbaTask);

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
                .load("hdfs://namenode-dba:9000/user/hive/warehouse/dba.db/CO2.csv");

        for(ColumnDefinition definition : csvColumns)  {
            dataset1 = dataset1.withColumnRenamed(definition.sourceName,definition.finalName);
            dataset1 = dataset1.withColumn(definition.finalName, dataset1.col(definition.finalName).cast(definition.type));
        }

        dataset1.printSchema();
        dataset1.show(false);

        JavaRDD<Co2Dto> rdd = dataset1
                .withColumn("id", monotonically_increasing_id())
                .as(Encoders.bean(Co2Dto.class))
                .javaRDD();

        handleTask(spark, rdd, "jdbc:postgresql://postgres-data-dba:5432/postgres");
    }

    private static void datalakeTask(SparkSession spark){

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

        handleTask(spark, dataset.javaRDD(), "jdbc:postgresql://postgres-data:5432/postgres");
    }

    private static void handleTask(SparkSession spark, JavaRDD<Co2Dto> rdd, String urlPostgre){

        JavaRDD<Co2Entity> rddEntity = rdd.map(Main::mapCo2);

        Dataset<Row> result = spark.createDataFrame(rddEntity, Co2Entity.class);

        result.show();

        Dataset<Row> resultGrouped = result
                .groupBy("marque")
                .agg(
                        round(avg("bonusmalus"), 2),
                        round(avg("coutenergie"), 2),
                        round(avg("rejet"),2)
                )
                .withColumnRenamed("round(avg(bonusmalus), 2)","bonusmalus")
                .withColumnRenamed("round(avg(coutenergie), 2)","coutenergie")
                .withColumnRenamed("round(avg(rejet), 2)","rejet");

        resultGrouped.show();

        resultGrouped.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .format("jdbc")
                .option("url", urlPostgre)
                .option("dbtable", "datawarehouse.carbon")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }

    private static Co2Entity mapCo2(Co2Dto co2){

        Co2Entity entity = new Co2Entity();

        String marquemodel = co2.getMarquemodel().trim().split(" ")[0].replace("\"","");
        Double bonusmalus = mapBonusMalus(co2.getBonusmalus());
        Double rejection = Double.valueOf(co2.getRejection());
        Double energiecost = Double.valueOf(co2.getEnergiecost().trim().replace("\u00A0","").split("€")[0]);

        entity.setMarque(marquemodel);
        entity.setBonusmalus(bonusmalus);
        entity.setRejet(rejection);
        entity.setCoutenergie(energiecost);

        return entity;
    }

    private static Double mapBonusMalus(String stringToModify) {
        if(stringToModify.contains("-") && stringToModify.length() == 1) {
            return null;
        }

        if(stringToModify.contains("+")) {
            stringToModify = stringToModify.split("\\+")[1];
        }

        return Double.valueOf(stringToModify.trim().replace("\u00A0","").split("€")[0]);
    }
}
