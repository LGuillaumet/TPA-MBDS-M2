#!/bin/bash

hadoop fs -mkdir -p    /user/hive/warehouse/dba.db
hadoop fs -chmod g+w   /user/hive/warehouse/dba.db

for file in /uploadfiles/files/*.csv
do
    echo "Push $file to hdfs"
    hadoop fs -put $file /user/hive/warehouse/dba.db
done