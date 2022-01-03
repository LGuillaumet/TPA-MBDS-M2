package org.mbds.clients.tasks;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.mbds.clients.dto.MarketingDto;
import org.mbds.clients.interfaces.IMarketingSparkTask;

import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.mbds.clients.config.JobConfiguration.URL_DATALAKE_SAVE_POSTGRES;
import static org.mbds.clients.config.JobConfiguration.URL_PRESTO;

public class DatalakeMarketingTask {

    private DatalakeMarketingTask(){}

    private static final String clientQuery = "select * from cassandra.datalake.marketing";

    public static void task(SparkSession spark, IMarketingSparkTask sparkTask){
        Dataset<MarketingDto> dataset = spark.read()
                .format("jdbc")
                .option("url", URL_PRESTO)
                .option("query", clientQuery)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load()
                .withColumn("id", monotonically_increasing_id())
                .as(Encoders.bean(MarketingDto.class));

        dataset.printSchema();
        dataset.show(false);

        sparkTask.handleTask(spark, dataset.javaRDD(), URL_DATALAKE_SAVE_POSTGRES);
    }

}
