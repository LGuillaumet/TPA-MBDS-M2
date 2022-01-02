package org.mbds.cars.tasks;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.mbds.cars.dto.CarDto;
import org.mbds.cars.dto.CatalogueDto;
import org.mbds.cars.dto.RegistrationDto;
import org.mbds.cars.entities.CarEntity;
import org.mbds.cars.entities.CatalogueEntity;
import org.mbds.cars.entities.RegistrationEntity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.spark.sql.functions.monotonically_increasing_id;

public class CommonTask {

    private CommonTask(){}

    private static final String CAR_REGISTRATION_QUERY = String.format("select registration.id, registration.registrationid, registration.occasion, registration.prix, car_entity.id as idCar " +
            "from car_entity " +
            "inner join registration on " +
            "car_entity.marque == registration.marque and " +
            "car_entity.nom == registration.nom and " +
            "car_entity.puissance == registration.puissance and " +
            "car_entity.longueur == registration.longueur and " +
            "car_entity.nbplaces == registration.nbplaces and " +
            "car_entity.nbportes == registration.nbportes and " +
            "car_entity.couleur == registration.couleur");

    private static final String CAR_CATALOGUE_QUERY = String.format("select catalogue.occasion, catalogue.prix, car_entity.id as idCar " +
            "from car_entity " +
            "inner join catalogue on " +
            "car_entity.marque == catalogue.marque and " +
            "car_entity.nom == catalogue.nom and " +
            "car_entity.puissance == catalogue.puissance and " +
            "car_entity.longueur == catalogue.longueur and " +
            "car_entity.nbplaces == catalogue.nbplaces and " +
            "car_entity.nbportes == catalogue.nbportes and " +
            "car_entity.couleur == catalogue.couleur");

    private static final String[][] collectionMarque = new String[][] {
            { "Hyundaè", "Hyundai" }
    };
    private static final Map<String, String> mapMarque = Stream.of(collectionMarque).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static void task(SparkSession spark, Dataset<RegistrationDto> datasetRegistration, Dataset<CatalogueDto> datasetCatalogue, String urlPostgre) throws AnalysisException {

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

        Dataset<RegistrationEntity> registrationEntity = spark.sql(CAR_REGISTRATION_QUERY).as(Encoders.bean(RegistrationEntity.class));

        Dataset<CatalogueEntity> catalogueEntity = spark.sql(CAR_CATALOGUE_QUERY)
                .withColumn("id", monotonically_increasing_id())
                .as(Encoders.bean(CatalogueEntity.class));

        rddCarEntity = rddCarEntity.map(entity -> {
            String base = entity.getMarque();
            String marque = mapMarque.get(base);
            marque = (marque != null ? marque : base).toUpperCase();
            entity.setMarque(marque);
            return entity;
        });

        datasetCarEntity = spark.createDataFrame(rddCarEntity, CarEntity.class).as(Encoders.bean(CarEntity.class));

        registrationEntity.show(false);
        datasetCarEntity.show(false);
        catalogueEntity.show(false);

        savePostgres(urlPostgre,"datawarehouse.cars", datasetCarEntity);
        savePostgres(urlPostgre,"datawarehouse.catalogue", catalogueEntity);
        savePostgres(urlPostgre, "datawarehouse.registrations", registrationEntity);
    }

    private static void savePostgres(String url, String tablename, Dataset<?> dataset){
        dataset.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .option("cascadeTruncate", true)
                .format("jdbc")
                .option("url", url)
                .option("dbtable", tablename)
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }
}
