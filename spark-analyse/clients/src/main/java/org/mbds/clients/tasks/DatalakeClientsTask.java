package org.mbds.clients.tasks;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.mbds.share.dto.ClientDto;
import org.mbds.share.interfaces.IClientsSparkTask;

import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.mbds.clients.config.JobConfiguration.URL_DATALAKE_SAVE_POSTGRES;
import static org.mbds.clients.config.JobConfiguration.URL_PRESTO;

public class DatalakeClientsTask {

    private DatalakeClientsTask(){}

    private static final String clientQuery = "select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid" + " " +
            "from mongodb.datalake.clients" + " " +
            "union all" + " " +
            "select age, sexe, taux, situation, nbchildren, havesecondcar, registrationid" + " " +
            "from hive.datalake.clients";

    public static void task(SparkSession spark, IClientsSparkTask sparkTask){

        Dataset<ClientDto> dataset = spark.read()
                .format("jdbc")
                .option("url", URL_PRESTO)
                .option("query", clientQuery)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load()
                .withColumn("id", monotonically_increasing_id())
                .as(Encoders.bean(ClientDto.class));

        dataset.printSchema();
        dataset.show(false);

        sparkTask.handleTask(spark, dataset.javaRDD(), URL_DATALAKE_SAVE_POSTGRES);
    }
}
