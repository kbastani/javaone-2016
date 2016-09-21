package com.example.order;

import com.example.order.aggregate.OrderAggregate;
import com.example.order.command.*;
import com.example.order.domain.CreateOrderRequest;
import com.example.order.domain.UpdateOrderRequest;
import com.example.order.repository.OrderRepository;
import io.eventuate.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class OrderServiceV1 {

    private Log log = LogFactory.getLog(OrderServiceV1.class);

    private AggregateRepository<OrderAggregate, OrderCommand> aggregateRepository;
    private OrderRepository orderRepository;
    private EventuateAggregateStore eventuateAggregateStore;

    @Autowired
    public OrderServiceV1(AggregateRepository<OrderAggregate, OrderCommand> aggregateRepository,
                          OrderRepository orderRepository, EventuateAggregateStore eventuateAggregateStore) {
        this.aggregateRepository = aggregateRepository;
        this.orderRepository = orderRepository;
        this.eventuateAggregateStore = eventuateAggregateStore;
    }


    public OrderAggregate createOrder(CreateOrderRequest orderRequest) {
        EntityWithIdAndVersion<OrderAggregate> createdAggregate =
                Util.get(aggregateRepository.save(new CreateOrderCommand(orderRequest.getOrderStatus())));

        // Save using JPA too

        OrderAggregate orderAggregate = createdAggregate.getAggregate();
        orderAggregate.setId(createdAggregate.getEntityId());
        orderAggregate.setVersion(createdAggregate.getEntityVersion().asString());

        return orderRepository.save(orderAggregate);
    }

    public OrderAggregate getOrder(String orderNumber) throws ExecutionException, InterruptedException {
        // Retrieve order using JPA
        return orderRepository.findOrderByOrderNumber(orderNumber);
    }

    public OrderAggregate process(String orderNumber, OrderCommand cmd)
            throws ExecutionException, InterruptedException {

        // Load from JPA

        OrderAggregate oa = getOrder(orderNumber);

        // Update

        List<Event> events = oa.processCommand(cmd);
        Aggregates.applyEventsToMutableAggregate(oa, events);

        // Save to Eventuate

        EntityIdAndVersion outcome = Util.get(eventuateAggregateStore.update(OrderAggregate.class,
                new EntityIdAndVersion(oa.getId(), Int128.fromString(oa.getVersion())),
                events));

        // Update the version

        oa.setVersion(outcome.getEntityVersion().asString());

        return oa;
    }

    public OrderAggregate updateOrder(String id, UpdateOrderRequest updateOrderRequest)
            throws ExecutionException, InterruptedException {

        switch (updateOrderRequest.getStatus()) {
            case PENDING:
                return process(id, new PendingOrderCommand());

            case CONFIRMED:
                return process(id, new ConfirmOrderCommand());

            case SHIPPED:
                return process(id, new ShipOrderCommand());

            default:
                throw new IllegalArgumentException("Status not expected");
        }
    }
}
