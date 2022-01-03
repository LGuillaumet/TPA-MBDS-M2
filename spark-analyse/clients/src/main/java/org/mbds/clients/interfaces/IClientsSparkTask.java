package org.mbds.clients.interfaces;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import org.mbds.clients.dto.ClientDto;

public interface IClientsSparkTask {
    void handleTask(SparkSession spark, JavaRDD<ClientDto> rdd, String urlPostgres);
}
