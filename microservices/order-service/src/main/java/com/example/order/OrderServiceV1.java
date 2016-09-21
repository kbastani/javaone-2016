package com.example.order;

import com.example.order.aggregate.OrderAggregate;
import com.example.order.command.*;
import com.example.order.domain.CreateOrderRequest;
import com.example.order.domain.OrderStatus;
import com.example.order.domain.UpdateOrderRequest;
import com.example.order.event.OrderCreatedEvent;
import com.example.order.query.OrderQuery;
import com.example.order.repository.Order;
import com.example.order.repository.OrderRepository;
import io.eventuate.AggregateRepository;
import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EventuateAggregateStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class OrderServiceV1 {

    private Log log = LogFactory.getLog(OrderServiceV1.class);

    private AggregateRepository<OrderAggregate, OrderCommand> aggregateRepository;
    private OrderRepository orderRepository;
    private OrderQuery orderQuery;
    private EventuateAggregateStore eventStore;

    @Autowired
    public OrderServiceV1(AggregateRepository<OrderAggregate, OrderCommand> aggregateRepository,
                          OrderRepository orderRepository, OrderQuery orderQuery,
                          EventuateAggregateStore eventStore) {
        this.aggregateRepository = aggregateRepository;
        this.orderRepository = orderRepository;
        this.orderQuery = orderQuery;
        this.eventStore = eventStore;
    }

    public AggregateRepository<OrderAggregate, OrderCommand> getAggregateRepository() {
        return aggregateRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public OrderQuery getOrderQuery() {
        return orderQuery;
    }

    public Order createOrder(CreateOrderRequest orderRequest) throws ExecutionException, InterruptedException {
        return createAggregate(orderRequest)
                .handle((entity, t) -> {
                    Order orderResult = orderRepository.save(new Order(entity.getEntityId()));
                    orderResult.withAggregate(entity.getAggregate());
                    return CompletableFuture.completedFuture(orderResult);
                }).get().get();
    }

    public CompletableFuture<EntityWithIdAndVersion<OrderAggregate>> createAggregate(
            CreateOrderRequest orderRequest) {
        OrderAggregate order = new OrderAggregate();
        order.apply(new OrderCreatedEvent(OrderStatus.CREATED));
        return aggregateRepository.save(new CreateOrderCommand(orderRequest.getOrderStatus()));
    }

    public Order getOrder(String id) throws ExecutionException, InterruptedException {
        Order order = orderRepository.findOrderByOrderNumber(id);
        return eventStore.find(OrderAggregate.class, order.getEntityId())
                .handle((u, t) -> order.withAggregate(u.getEntity()))
                .get();
    }

    public Order process(Order order, OrderCommand cmd)
            throws ExecutionException, InterruptedException {
        return aggregateRepository.update(order.getEntityId(), cmd)
                .handle((o, t) -> order.withAggregate(o.getAggregate()))
                .get();
    }

    public Order updateOrder(String id, UpdateOrderRequest updateOrderRequest)
            throws ExecutionException, InterruptedException {
        Order order = getOrder(id);
        Assert.notNull(order);

        switch (updateOrderRequest.getStatus()) {
            case PENDING:
                order = process(order, new PendingOrderCommand());
                break;
            case CONFIRMED:
                order = process(order, new ConfirmOrderCommand());
                break;
            case SHIPPED:
                order = process(order, new ShipOrderCommand());
                break;
            default:
                throw new IllegalArgumentException("Status not expected");
        }

        return order;
    }
}
