package org.mbds.cars.tasks;

import org.apache.spark.sql.*;
import org.mbds.share.dto.CatalogueDto;
import org.mbds.share.dto.RegistrationDto;
import org.mbds.share.interfaces.ISparkTask;
import org.mbds.share.models.ColumnDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.types.DataTypes.*;
import static org.mbds.cars.config.JobConfiguration.*;

public class DbaTask {

    private DbaTask(){}

    private static final List<ColumnDefinition> csvRegistrationColumns = new ArrayList(Arrays.asList(
            new ColumnDefinition("immatriculation", "registrationid", StringType),
            new ColumnDefinition("marque", "marque", StringType),
            new ColumnDefinition("nom", "nom", StringType),
            new ColumnDefinition("puissance", "puissance", LongType),
            new ColumnDefinition("longueur", "longueur", StringType),
            new ColumnDefinition("nbPlaces", "nbplaces", LongType),
            new ColumnDefinition("nbPortes", "nbportes", LongType),
            new ColumnDefinition("couleur", "couleur", StringType),
            new ColumnDefinition("occasion", "occasion", BooleanType),
            new ColumnDefinition("prix", "prix", DoubleType)
    ));

    private static final List<ColumnDefinition> csvCatalogueColumns = new ArrayList(Arrays.asList(
            new ColumnDefinition("marque", "marque", StringType),
            new ColumnDefinition("nom", "nom", StringType),
            new ColumnDefinition("puissance", "puissance", LongType),
            new ColumnDefinition("longueur", "longueur", StringType),
            new ColumnDefinition("nbPlaces", "nbplaces", LongType),
            new ColumnDefinition("nbPortes", "nbportes", LongType),
            new ColumnDefinition("couleur", "couleur", StringType),
            new ColumnDefinition("occasion", "occasion", BooleanType),
            new ColumnDefinition("prix", "prix", DoubleType)
    ));

    public static void task(SparkSession spark, ISparkTask sparkTask) throws AnalysisException {

        Dataset<Row> datasetRegistration = spark.read()
                .format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .load(URL_HDFS_REGISTRATION);

        Dataset<Row> datasetCatalogue = spark.read()
                .format("csv")
                .option("header", "true")
                .option("delimiter", ",")
                .load(URL_HDFS_CATALOGUE);

        for(ColumnDefinition definition : csvRegistrationColumns)  {
            datasetRegistration = datasetRegistration.withColumnRenamed(definition.sourceName,definition.finalName);
            datasetRegistration = datasetRegistration.withColumn(definition.finalName, datasetRegistration.col(definition.finalName).cast(definition.type));
        }

        datasetRegistration = datasetRegistration.withColumn("id", monotonically_increasing_id().cast(StringType));

        datasetRegistration.printSchema();
        datasetRegistration.show(false);

        for(ColumnDefinition definition : csvCatalogueColumns)  {
            datasetCatalogue = datasetCatalogue.withColumnRenamed(definition.sourceName,definition.finalName);
            datasetCatalogue = datasetCatalogue.withColumn(definition.finalName, datasetCatalogue.col(definition.finalName).cast(definition.type));
        }

        datasetCatalogue.printSchema();
        datasetCatalogue.show(false);

        sparkTask.handleTask(spark,
                datasetRegistration.as(Encoders.bean(RegistrationDto.class)),
                datasetCatalogue.as(Encoders.bean(CatalogueDto.class)),
                URL_HDFS_SAVE_POSTGRES
        );
    }
}
