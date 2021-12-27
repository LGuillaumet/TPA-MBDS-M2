#!/usr/bin/env bash

echo "Waiting start broker."

/usr/local/share/landoop/wait-scripts/wait-for-kafka.sh

echo "Create topic if not exists."

/opt/landoop/kafka/bin/kafka-topics --create --if-not-exists --zookeeper localhost:3181 --partitions 4 --replication-factor 1 --topic upserts-marketing-cassandra
/opt/landoop/kafka/bin/kafka-topics --create --if-not-exists --zookeeper localhost:3181 --partitions 4 --replication-factor 1 --topic upserts-registration-cassandra
/opt/landoop/kafka/bin/kafka-topics --create --if-not-exists --zookeeper localhost:3181 --partitions 4 --replication-factor 1 --topic upserts-catalogue-hive
/opt/landoop/kafka/bin/kafka-topics --create --if-not-exists --zookeeper localhost:3181 --partitions 4 --replication-factor 1 --topic upserts-clients-hive
/opt/landoop/kafka/bin/kafka-topics --create --if-not-exists --zookeeper localhost:3181 --partitions 4 --replication-factor 1 --topic upserts-carbon-mongo
/opt/landoop/kafka/bin/kafka-topics --create --if-not-exists --zookeeper localhost:3181 --partitions 4 --replication-factor 1 --topic upserts-clients-mongo