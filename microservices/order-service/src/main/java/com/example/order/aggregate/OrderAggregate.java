package com.example.order.aggregate;

import com.example.order.command.*;
import com.example.order.domain.OrderStatus;
import com.example.order.event.OrderConfirmedEvent;
import com.example.order.event.OrderCreatedEvent;
import com.example.order.event.OrderPendingEvent;
import com.example.order.event.OrderShippedEvent;
import com.example.order.repository.Order;
import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * This class defines the OrderAggregate aggregate which aggregates the state of an
 * {@link Order} domain object.
 */
public class OrderAggregate extends ReflectiveMutableCommandProcessingAggregate<OrderAggregate, OrderCommand> {

    private final Log log = LogFactory.getLog(OrderAggregate.class);

    protected OrderStatus status;

    public List<Event> process(CreateOrderCommand cmd) {
        log.debug("Process CreateOrderCommand...");
        return EventUtil.events(new OrderCreatedEvent(cmd.getInitialStatus()));
    }

    public List<Event> process(PendingOrderCommand cmd) {
        log.debug("Process PendingOrderCommand...");
        return EventUtil.events(new OrderPendingEvent());
    }

    public List<Event> process(ConfirmOrderCommand cmd) {
        log.debug("Process ConfirmOrderCommand...");
        return EventUtil.events(new OrderConfirmedEvent());
    }

    public List<Event> process(ShipOrderCommand cmd) {
        log.debug("Process ShipOrderCommand...");
        return EventUtil.events(new OrderShippedEvent());
    }

    public void apply(OrderCreatedEvent event) {
        status = event.getInitialStatus();
    }

    public void apply(OrderPendingEvent event) {
        status = OrderStatus.PENDING;
    }

    public void apply(OrderConfirmedEvent event) {
        status = OrderStatus.CONFIRMED;
    }

    public void apply(OrderShippedEvent event) {
        status = OrderStatus.SHIPPED;
    }

    public OrderStatus getStatus() {
        return status;
    }
}