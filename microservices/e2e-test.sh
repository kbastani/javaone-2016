#!/bin/bash

set -e

SERVICE_HOST=localhost

if [  -z "$1"  ] ; then
    SERVICE_HOST=localhost
else
    SERVICE_HOST=${1?}
fi

echo SERVICE_HOST=$SERVICE_HOST

echo "Create new order..."

ORDER_NUMBER=$(curl -X POST -H "Content-Type: application/json" \
    -s -d '{ "orderStatus": "CREATED" }' \
    "http://${SERVICE_HOST}:8080/v1/orders" | jq '.orderNumber' | sed 's/"//g')

echo "Order creation successful: ${ORDER_NUMBER}"
echo "GET http://${SERVICE_HOST}:8080/v1/orders/${ORDER_NUMBER}"

ORDER_OBJECT=$(curl -s -X GET "http://${SERVICE_HOST}:8080/v1/orders/${ORDER_NUMBER}")

echo "${ORDER_OBJECT}" | jq "."

sleep 1

echo "Update Order Status: CREATED >> PENDING"

ORDER_OBJECT=$(curl -s -X PUT -H "Content-Type: application/json" "http://${SERVICE_HOST}:8080/v1/orders/${ORDER_NUMBER}" -d '{"status": "PENDING"}')

echo "Result:"
echo "${ORDER_OBJECT}" | jq "."

sleep 1

echo "Update Order Status: PENDING >> CONFIRMED"

ORDER_OBJECT=$(curl -s -X PUT -H "Content-Type: application/json" "http://${SERVICE_HOST}:8080/v1/orders/${ORDER_NUMBER}" -d '{"status": "CONFIRMED"}')

echo "Result:"
echo "${ORDER_OBJECT}" | jq "."

sleep 1

echo "Update Order Status: CONFIRMED >> SHIPPED"

ORDER_OBJECT=$(curl -s -X PUT -H "Content-Type: application/json" "http://${SERVICE_HOST}:8080/v1/orders/${ORDER_NUMBER}" -d '{"status": "SHIPPED"}')

echo "Result:"
echo "${ORDER_OBJECT}" | jq "."

sleep 1

echo "Tests complete!"
