package org.mbds.cars.interfaces;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.mbds.cars.dto.CatalogueDto;
import org.mbds.cars.dto.RegistrationDto;

public interface ISparkTask {
    void handleTask(SparkSession spark, Dataset<RegistrationDto> datasetRegistration, Dataset<CatalogueDto> datasetCatalogue, String urlPostgre) throws AnalysisException;
}
