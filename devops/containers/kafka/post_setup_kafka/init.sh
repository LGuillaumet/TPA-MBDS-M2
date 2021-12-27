#!/usr/bin/env bash

echo 'SETUP CONNECTORS'

/post_setup_kafka/create_topics.sh &
/post_setup_kafka/push_connectors.sh & 


