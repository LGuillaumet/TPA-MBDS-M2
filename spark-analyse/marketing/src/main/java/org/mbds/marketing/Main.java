package org.mbds.marketing;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.mbds.share.interfaces.IMarketingSparkAction;
import org.mbds.marketing.tasks.CommonMarketingTask;
import org.mbds.marketing.tasks.DatalakeMarketingTask;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final Map<String, IMarketingSparkAction> mapActionMarketing = new HashMap<>();

    public static void main(String[] args) {

        mapActionMarketing.put("datalake", DatalakeMarketingTask::task);
        mapActionMarketing.put("dba", DatalakeMarketingTask::task);

        SparkSession spark = getSession();

        if(args.length > 0){
            IMarketingSparkAction sparkactionmarketing = mapActionMarketing.get(args[0]);
            if(sparkactionmarketing != null){
                sparkactionmarketing.handle(spark, CommonMarketingTask::task);
            }
        }

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

        return SparkSession.builder().config(configuration).getOrCreate();
    }
}
