package org.mbds.marketing.tasks;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.mbds.marketing.config.JobConfiguration;
import org.mbds.share.dto.MarketingDto;
import org.mbds.share.interfaces.IMarketingSparkTask;
import org.mbds.share.models.ColumnDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.types.DataTypes.*;
import static org.apache.spark.sql.types.DataTypes.StringType;


public class DbaMarketingTask {

    private DbaMarketingTask(){}

    private static final List<ColumnDefinition> csvColumns = new ArrayList(Arrays.asList(
            new ColumnDefinition("age", "age", LongType),
            new ColumnDefinition("sexe", "sexe", StringType),
            new ColumnDefinition("taux", "taux", LongType),
            new ColumnDefinition("situationFamiliale", "situation", StringType),
            new ColumnDefinition("nbEnfantsAcharge", "nbchildren", LongType),
            new ColumnDefinition("2eme voiture", "havesecondcar", BooleanType)
    ));

    public static void task(SparkSession spark, IMarketingSparkTask sparkTask){

        Dataset<Row> dataset = spark.read()
                .format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .load(JobConfiguration.URL_HDFS_MARKETING);

        for(ColumnDefinition definition : csvColumns)  {
            dataset = dataset.withColumnRenamed(definition.sourceName,definition.finalName);
            dataset = dataset.withColumn(definition.finalName, dataset.col(definition.finalName).cast(definition.type));
        }

        dataset.printSchema();
        dataset.show(false);

        JavaRDD<MarketingDto> rdd = dataset
                .withColumn("id", monotonically_increasing_id().cast(StringType))
                .as(Encoders.bean(MarketingDto.class))
                .javaRDD();

        sparkTask.handleTask(spark, rdd, JobConfiguration.URL_HDFS_SAVE_POSTGRES);
    }

}
