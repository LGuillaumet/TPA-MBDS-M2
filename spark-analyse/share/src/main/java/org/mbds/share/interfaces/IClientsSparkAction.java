package org.mbds.share.interfaces;

import org.apache.spark.sql.SparkSession;

public interface IClientsSparkAction {
    void handle(SparkSession spark, IClientsSparkTask next);
}
