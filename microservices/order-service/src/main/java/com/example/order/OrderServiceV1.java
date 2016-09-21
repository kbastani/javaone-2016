package com.example.order;

import com.example.order.aggregate.OrderAggregate;
import com.example.order.command.*;
import com.example.order.domain.CreateOrderRequest;
import com.example.order.domain.UpdateOrderRequest;
import com.example.order.repository.OrderRepository;
import io.eventuate.AggregateRepository;
import io.eventuate.EntityWithIdAndVersion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class OrderServiceV1 {

    private Log log = LogFactory.getLog(OrderServiceV1.class);

    private AggregateRepository<OrderAggregate, OrderCommand> aggregateRepository;
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceV1(AggregateRepository<OrderAggregate, OrderCommand> aggregateRepository,
                          OrderRepository orderRepository) {
        this.aggregateRepository = aggregateRepository;
        this.orderRepository = orderRepository;
    }


    public OrderAggregate createOrder(CreateOrderRequest orderRequest) {
        EntityWithIdAndVersion<OrderAggregate> entity = Util.get(aggregateRepository.save(new CreateOrderCommand(orderRequest.getOrderStatus())));

        // Save using JPA too

        entity.getAggregate().setId(entity.getEntityId());
        return orderRepository.save(entity.getAggregate());
    }

    public OrderAggregate getOrder(String orderNumber) throws ExecutionException, InterruptedException {
        // Retrieve order using JPA
        return orderRepository.findOrderByOrderNumber(orderNumber);
    }

    public OrderAggregate process(String orderNumber, OrderCommand cmd)
            throws ExecutionException, InterruptedException {
        OrderAggregate oa = getOrder(orderNumber);
        EntityWithIdAndVersion<OrderAggregate> entity = Util.get(aggregateRepository.update(oa.getId(), cmd));

        // Update via JPA too
        entity.getAggregate().setId(entity.getEntityId());
        orderRepository.save(entity.getAggregate());
        return entity.getAggregate();
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
