package org.mbds.share.interfaces;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.mbds.share.dto.ClientDto;

public interface IClientsSparkTask {
    void handleTask(SparkSession spark, JavaRDD<ClientDto> rdd, String urlPostgres);
}
