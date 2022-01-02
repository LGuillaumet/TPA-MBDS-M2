package org.mbds.clients.interfaces;

import org.apache.spark.sql.SparkSession;

public interface ISparkAction {
    void handle(SparkSession spark, ISparkTask next);
}
