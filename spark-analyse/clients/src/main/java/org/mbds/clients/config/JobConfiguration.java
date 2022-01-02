package org.mbds.clients.config;

public class JobConfiguration {

    private JobConfiguration(){}

    public static final String URL_HDFS_CLIENT_1 = "hdfs://namenode-dba:9000/user/hive/warehouse/dba.db/Clients_1.csv";
    public static final String URL_HDFS_CLIENT_9 = "hdfs://namenode-dba:9000/user/hive/warehouse/dba.db/Clients_9.csv";
    public static final String URL_HDFS_SAVE_POSTGRES = "jdbc:postgresql://postgres-data-dba:5432/postgres";

    public static final String URL_PRESTO = "jdbc:presto://presto:8080";
    public static final String URL_DATALAKE_SAVE_POSTGRES = "jdbc:postgresql://postgres-data:5438/postgres";


}
