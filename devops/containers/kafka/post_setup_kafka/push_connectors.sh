#!/usr/bin/env bash

url="http://localhost:7083/connectors"

echo "Waiting for Kafka Connect to start listening on kafka-connect ⏳"
code=$(curl -s -o /dev/null -w %{http_code} $url)
echo $code
while [ $code -eq 000 ] ; do 
    dateStr=$(date)
    echo -e $dateStr " Kafka Connect listener HTTP state: " $code " (waiting for 200)"
    sleep 5 
    code=$(curl -s -o /dev/null -w %{http_code} $url)
done

#nc -vz localhost 7083
sleep 10

for config in /post_setup_kafka/configkafka/*.json
do
    echo "Push $config to Kafka Connect"
    curl -d @$config -H "Content-Type: application/json" -X POST $url
done
