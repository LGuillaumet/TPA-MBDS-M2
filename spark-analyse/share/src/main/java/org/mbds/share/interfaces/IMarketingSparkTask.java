package org.mbds.share.interfaces;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.mbds.share.dto.MarketingDto;

public interface IMarketingSparkTask {
    void handleTask(SparkSession spark, JavaRDD<MarketingDto> rdd, String urlPostgres);
}
