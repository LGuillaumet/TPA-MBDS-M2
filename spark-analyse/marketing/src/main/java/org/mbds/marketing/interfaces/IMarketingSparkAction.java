package org.mbds.marketing.interfaces;

import org.apache.spark.sql.SparkSession;

public interface IMarketingSparkAction {
    void handle(SparkSession spark, IMarketingSparkTask next);
}
