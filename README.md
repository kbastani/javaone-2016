# JavaOne 2016 Demos

This repository contains demos for JavaOne 2016.

## Handling Eventual Consistency in JVM Microservices with Event Sourcing

When you’re building JVM applications in a microservice architecture, managing state becomes a distributed systems problem. Instead of being able to manage state as transactions inside the boundaries of a single monolithic application, a microservice must be able to manage consistency by using transactions that are distributed across a network of many different applications and databases. This session explores the problems of data consistency and high availability in JVM-based microservices and how to use event sourcing to solve these problems.

Wednesday, Sep 21, 3:00 p.m. - 4:00 p.m. | Parc 55 - Embarcadero

## Usage

Setup your [Eventuate](http://eventuate.io) local environment by running Docker Compose in the `/microservices` directory.

    $ docker-compose -f docker-compose-eventuate-local.yml up -d

After each of the Eventuate containers are started, setup your environment using the `/microservices/set-env.sh` script. This version of the demo requires Docker Machine to retrieve the DOCKER_HOST_IP.
    
    $ eval $(docker-machine env default)
    $ sh ./microservices/set-env.sh
    DOCKER_HOST_IP is 192.168.99.100

Now that the environment has been setup, you can run the example order service.

    $ cd ./microservices/order-service
    $ mvn spring-boot:run

If the environment is properly configured, the `order-service` will start up and point to the Eventuate Local containers we started with Docker Compose. The application will start up on http://localhost:8080. To test that everything is working correctly, run the `e2e-test.sh` script.

    $ sh ./microservices/e2e-test.sh
    
    Create new order...
    Order creation successful: 6cc64067-b085-4ce7-b9a5-b63bd9f74714
    GET http://localhost:8080/v1/orders/6cc64067-b085-4ce7-b9a5-b63bd9f74714
    {
      "status": "CREATED",
      "id": 35,
      "entityId": "000001574a80cf8a-52d57e06d3510001",
      "orderNumber": "6cc64067-b085-4ce7-b9a5-b63bd9f74714"
    }
    Update Order Status: CREATED >> PENDING
    Result:
    {
      "status": "PENDING",
      "id": 35,
      "entityId": "000001574a80cf8a-52d57e06d3510001",
      "orderNumber": "6cc64067-b085-4ce7-b9a5-b63bd9f74714"
    }
    Update Order Status: PENDING >> CONFIRMED
    Result:
    {
      "status": "CONFIRMED",
      "id": 35,
      "entityId": "000001574a80cf8a-52d57e06d3510001",
      "orderNumber": "6cc64067-b085-4ce7-b9a5-b63bd9f74714"
    }
    Update Order Status: CONFIRMED >> SHIPPED
    Result:
    {
      "status": "SHIPPED",
      "id": 35,
      "entityId": "000001574a80cf8a-52d57e06d3510001",
      "orderNumber": "6cc64067-b085-4ce7-b9a5-b63bd9f74714"
    }
    Tests complete!

The `e2e-test.sh` script will create a new order and transition the state from `CREATED` to `SHIPPED`. Notice that the `Order` domain object does not have a status field persisted to MySQL. The `Order` object extends the `OrderAggregate` object, which uses Eventuate to aggregate the status of the order from a sequence of events. Pretty cool!