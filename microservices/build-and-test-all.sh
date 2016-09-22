#! /bin/bash

set -e

. ./set-env.sh


docker-compose -f docker-compose-eventuate-local.yml -f docker-compose.yml stop
docker-compose -f docker-compose-eventuate-local.yml -f docker-compose.yml rm -v --force

docker-compose -f docker-compose-eventuate-local.yml up -d

mvn package

docker-compose -f docker-compose-eventuate-local.yml -f docker-compose.yml build

docker-compose -f docker-compose-eventuate-local.yml -f docker-compose.yml up -d

./wait-for-services.sh $DOCKER_HOST_IP 8080

echo testing. look for successful message

./e2e-test.sh $DOCKER_HOST_IP

echo SUCCESS
