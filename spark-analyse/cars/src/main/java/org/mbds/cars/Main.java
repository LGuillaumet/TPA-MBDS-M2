package org.mbds.cars;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.mbds.cars.dto.CarDto;
import org.mbds.cars.dto.CatalogueDto;
import org.mbds.cars.dto.RegistrationDto;
import org.mbds.cars.entities.CarEntity;
import org.mbds.cars.entities.CatalogueEntity;
import org.mbds.cars.entities.RegistrationEntity;

import static org.apache.spark.sql.functions.monotonically_increasing_id;

public class Main {

    private static final String carsRegistrationQuery = String.format("select registration.registrationid as id, registration.occasion, registration.prix, car_entity.id as idCar " +
            "from car_entity " +
            "inner join registration on " +
            "car_entity.marque == registration.marque and " +
            "car_entity.nom == registration.nom and " +
            "car_entity.puissance == registration.puissance and " +
            "car_entity.longueur == registration.longueur and " +
            "car_entity.nbplaces == registration.nbplaces and " +
            "car_entity.nbportes == registration.nbportes and " +
            "car_entity.couleur == registration.couleur");

    private static final String carsCatalogueQuery = String.format("select catalogue.occasion, catalogue.prix, car_entity.id as idCar " +
            "from car_entity " +
            "inner join catalogue on " +
            "car_entity.marque == catalogue.marque and " +
            "car_entity.nom == catalogue.nom and " +
            "car_entity.puissance == catalogue.puissance and " +
            "car_entity.longueur == catalogue.longueur and " +
            "car_entity.nbplaces == catalogue.nbplaces and " +
            "car_entity.nbportes == catalogue.nbportes and " +
            "car_entity.couleur == catalogue.couleur");

    public static void main(String[] args) throws AnalysisException {
        //
        // The "modern" way to initialize Spark is to create a SparkSession
        // although they really come from the world of Spark SQL, and Dataset
        // and DataFrame.
        //

        SparkSession spark = getSession();
        Dataset<RegistrationDto> datasetRegistration = loadPrestoDataset(spark, "select * from cassandra.datalake.registration", null).as(Encoders.bean(RegistrationDto.class));
        Dataset<CatalogueDto> datasetCatalogue = loadPrestoDataset(spark, "select * from hive.datalake.catalogue", "id").as(Encoders.bean(CatalogueDto.class));

        JavaRDD<RegistrationDto> rddR = datasetRegistration.javaRDD();
        JavaRDD<CatalogueDto> rddC = datasetCatalogue.javaRDD();

        JavaRDD<CarDto> carRegistration = rddR.map(CarDto::new);
        JavaRDD<CarDto> carCatalogue = rddC.map(CarDto::new);

        JavaRDD<CarDto> rddCar = carRegistration
                .union(carCatalogue)
                .distinct();

        JavaRDD<CarEntity> rddCarEntity = rddCar.map(CarEntity::new)
                .distinct()
                .zipWithUniqueId()
                .map(tuple -> { tuple._1.setId(tuple._2); return tuple._1;});


        Dataset<CarEntity> datasetCarEntity = spark.createDataFrame(rddCarEntity, CarEntity.class).as(Encoders.bean(CarEntity.class));

        datasetRegistration.createTempView("registration");
        datasetCarEntity.createTempView("car_entity");
        datasetCatalogue.createTempView("catalogue");

        Dataset<RegistrationEntity> registrationEntity = spark.sql(carsRegistrationQuery).as(Encoders.bean(RegistrationEntity.class));
        Dataset<CatalogueEntity> catalogueEntity = spark.sql(carsCatalogueQuery).withColumn("id", monotonically_increasing_id()).as(Encoders.bean(CatalogueEntity.class));

        registrationEntity.show(false);
        datasetCarEntity.show(false);
        catalogueEntity.show(false);

        savePostgres("datawarehouse.cars", datasetCarEntity);
        savePostgres("datawarehouse.catalogue", catalogueEntity);
        savePostgres("datawarehouse.registrations", registrationEntity);

        spark.stop();
    }

    private static SparkSession getSession(){
        SparkConf configuration = new SparkConf()
                .setAppName("Cars-job")
                .setMaster("spark://spark:7077")
                .set("spark.submit.deployMode", "client")
                .set("spark.ui.showConsoleProgress", "true")
                .set("spark.eventLog.enabled", "false")
                .set("spark.logConf", "false")
                .set("spark.driver.bindAddress", "0.0.0.0")
                .set("spark.driver.host", "spark");

        return SparkSession
                .builder()
                .config(configuration)
                .getOrCreate();
    }

    private static Dataset<Row> loadPrestoDataset(SparkSession spark, String query, String nameColId){
        Dataset<Row> dataset = spark.read()
                .format("jdbc")
                .option("url", "jdbc:presto://presto:8080")
                .option("query", query)
                .option("user", "user")
                .option("driver", "com.facebook.presto.jdbc.PrestoDriver")
                .load();

        if(nameColId != null && !nameColId.equals("")){
            dataset.withColumn(nameColId, monotonically_increasing_id());
        }

        return dataset;
    }

    private static void savePostgres(String tablename, Dataset<?> dataset){
        dataset.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .option("cascadeTruncate", true)
                .format("jdbc")
                .option("url", "jdbc:postgresql://postgres-data:5438/postgres")
                .option("dbtable", tablename)
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }
}
