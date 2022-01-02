package org.mbds.cars.tasks;

import org.apache.spark.sql.*;
import org.mbds.cars.dto.CatalogueDto;
import org.mbds.cars.dto.RegistrationDto;
import org.mbds.cars.interfaces.ISparkTask;

import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.mbds.cars.config.JobConfiguration.URL_DATALAKE_SAVE_POSTGRES;
import static org.mbds.cars.config.JobConfiguration.URL_PRESTO;

public class DatalakeTask {

    private DatalakeTask(){}

    public static void task(SparkSession spark, ISparkTask sparkTask) throws AnalysisException {
        Dataset<RegistrationDto> datasetRegistration = loadPrestoDataset(spark, "select * from cassandra.datalake.registration", null).as(Encoders.bean(RegistrationDto.class));
        Dataset<CatalogueDto> datasetCatalogue = loadPrestoDataset(spark, "select * from hive.datalake.catalogue", "id").as(Encoders.bean(CatalogueDto.class));

        sparkTask.handleTask(spark, datasetRegistration, datasetCatalogue, URL_DATALAKE_SAVE_POSTGRES);
    }

    private static Dataset<Row> loadPrestoDataset(SparkSession spark, String query, String nameColId){
        Dataset<Row> dataset = spark.read()
                .format("jdbc")
                .option("url", URL_PRESTO)
                .option("query", query)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load();

        if(nameColId != null && !nameColId.equals("")){
            dataset.withColumn(nameColId, monotonically_increasing_id());
        }

        return dataset;
    }
}
