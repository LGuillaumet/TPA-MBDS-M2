package org.mbds.marketing.tasks;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.mbds.marketing.config.JobConfiguration;
import org.mbds.share.dto.MarketingDto;
import org.mbds.share.interfaces.IMarketingSparkTask;


public class DatalakeMarketingTask {

    private DatalakeMarketingTask(){}

    private static final String clientQuery = "select * from cassandra.datalake.marketing";

    public static void task(SparkSession spark, IMarketingSparkTask sparkTask){
        Dataset<MarketingDto> dataset = spark.read()
                .format("jdbc")
                .option("url", JobConfiguration.URL_PRESTO)
                .option("query", clientQuery)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load()
                .as(Encoders.bean(MarketingDto.class));

        dataset.printSchema();
        dataset.show(false);

        sparkTask.handleTask(spark, dataset.javaRDD(), JobConfiguration.URL_DATALAKE_SAVE_POSTGRES);
    }
}
