package org.mbds.share.interfaces;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;

public interface ISparkAction {
    void handle(SparkSession spark, ISparkTask next) throws AnalysisException;
}
