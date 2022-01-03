package org.mbds.clients.tasks;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.mbds.clients.dto.ClientDto;
import org.mbds.clients.interfaces.IClientsSparkTask;
import org.mbds.clients.models.ColumnDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.types.DataTypes.*;
import static org.apache.spark.sql.types.DataTypes.StringType;
import static org.mbds.clients.config.JobConfiguration.*;

public class DbaClientsTask {

    private DbaClientsTask(){}

    private static final List<ColumnDefinition> csvColumns = new ArrayList(Arrays.asList(
            new ColumnDefinition("age", "age", LongType),
            new ColumnDefinition("sexe", "sexe", StringType),
            new ColumnDefinition("taux", "taux", LongType),
            new ColumnDefinition("situationFamiliale", "situation", StringType),
            new ColumnDefinition("nbEnfantsAcharge", "nbchildren", LongType),
            new ColumnDefinition("2eme voiture", "havesecondcar", BooleanType),
            new ColumnDefinition("immatriculation", "registrationid", StringType)
    ));

    public static void task(SparkSession spark, IClientsSparkTask sparkTask){

        Dataset<Row> dataset1 = spark.read()
                .format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .load(URL_HDFS_CLIENT_1);

        Dataset<Row> dataset9 = spark.read()
                .format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .load(URL_HDFS_CLIENT_9);

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

        sparkTask.handleTask(spark, rdd, URL_HDFS_SAVE_POSTGRES);
    }
}
