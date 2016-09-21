package com.example.order.query;

import com.example.order.OrderServiceV1;
import com.example.order.event.OrderConfirmedEvent;
import com.example.order.event.OrderCreatedEvent;
import com.example.order.event.OrderPendingEvent;
import com.example.order.event.OrderShippedEvent;
import io.eventuate.DispatchedEvent;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EventSubscriber(id = "querySideEventHandlers")
public class OrderQuery {

    private OrderServiceV1 orderServiceV1;
    private final Log log = LogFactory.getLog(OrderQuery.class);

    public OrderQuery() {
    }

    @Autowired
    public OrderQuery(OrderServiceV1 orderServiceV1) {
        this.orderServiceV1 = orderServiceV1;
    }

    @EventHandlerMethod
    public void create(DispatchedEvent<OrderCreatedEvent> de) {
        log.info("Order created: " + de.getEvent());
    }

    @EventHandlerMethod
    public void pending(DispatchedEvent<OrderPendingEvent> de) {
        log.info("Order pending: " + de.getEvent());
    }

    @EventHandlerMethod
    public void confirmed(DispatchedEvent<OrderConfirmedEvent> de) {
        log.info("Order confirmed: " + de.getEvent());
    }

    @EventHandlerMethod
    public void shipped(DispatchedEvent<OrderShippedEvent> de) {
        log.info("Order shipped: " + de.getEvent());
    }

}
