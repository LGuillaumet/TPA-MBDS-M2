package org.mbds.marketing.interfaces;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.mbds.marketing.dto.MarketingDto;

public interface IMarketingSparkTask {
    void handleTask(SparkSession spark, JavaRDD<MarketingDto> rdd, String urlPostgres);
}
