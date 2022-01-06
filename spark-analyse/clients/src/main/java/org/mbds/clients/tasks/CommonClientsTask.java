package org.mbds.clients.tasks;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.mbds.share.dto.ClientDto;
import org.mbds.share.entities.ClientEntity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonClientsTask {

    private CommonClientsTask(){}

    private static final String[][] collectionSexe = new String[][] {
            { "Femme", "F" },
            { "F", "F" },
            { "Féminin", "F" },
            { "Homme", "M" },
            { "M", "M" },
            { "Masculin", "M" }
    };

    private static final String[][] collectionSituation = new String[][] {
            { "En Couple", "Couple" },
            { "Divorcé", "Divorced" },
            { "Divorcée", "Divorced" },
            { "Célibataire", "Single" },
            { "Seul", "Single" },
            { "Seule", "Single" },
            { "Marié", "Married" },
            { "Marié(e)", "Married" }
    };

    private static final Map<String, String> mapSexe = Stream.of(collectionSexe).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    private static final Map<String, String> mapSituation = Stream.of(collectionSituation).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static void task(SparkSession spark, JavaRDD<ClientDto> rdd, String urlPostgre){

        JavaRDD<ClientEntity> rddEntity = rdd.map(CommonClientsTask::mapClient);

        Dataset<Row> result = spark.createDataFrame(rddEntity, ClientEntity.class);
        result.show(false);

        result.write()
                .mode(SaveMode.Overwrite)
                .option("truncate", true)
                .format("jdbc")
                .option("url", urlPostgre)
                .option("dbtable", "datawarehouse.clients")
                .option("user", "postgres")
                .option("password", "postgres")
                .option("driver", "org.postgresql.Driver")
                .save();
    }

    private static ClientEntity mapClient(ClientDto client){

        ClientEntity entity = new ClientEntity();

        Integer age = longToInteger(client.getAge());
        String sexe = client.getSexe();
        Integer taux = longToInteger(client.getTaux());
        String situation = client.getSituation();
        Integer nbchildren = longToInteger(client.getNbchildren());
        Boolean havesecondcar = client.getHavesecondcar();
        String registrationid = client.getRegistrationid();

        age = age != null && age > 0 ? age : null;
        sexe = mapSexe.get(sexe);
        taux = taux != null && taux >= 0 ? taux : null;
        situation = mapSituation.get(situation);
        nbchildren = nbchildren != null && nbchildren >= 0 ? nbchildren : null;

        entity.setId(client.getId());
        entity.setAge(age);
        entity.setSexe(sexe);
        entity.setTaux(taux);
        entity.setSituation(situation);
        entity.setNbchildren(nbchildren);
        entity.setHavesecondcar(havesecondcar);
        entity.setRegistrationid(registrationid);

        return entity;
    }

    private static Integer longToInteger(Long value){
        if(value == null) return null;
        return Math.toIntExact(value);
    }
}
