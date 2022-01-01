#!/usr/bin/env bash

echo 'Create topics and setup connectors'

/init_kafka/create_topics.sh &
/init_kafka/push_connectors.sh & 