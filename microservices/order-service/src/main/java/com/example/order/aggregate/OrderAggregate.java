package com.example.order.aggregate;

import com.example.order.command.*;
import com.example.order.domain.OrderStatus;
import com.example.order.event.OrderConfirmedEvent;
import com.example.order.event.OrderCreatedEvent;
import com.example.order.event.OrderPendingEvent;
import com.example.order.event.OrderShippedEvent;
import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;
import java.util.UUID;

/**
 * This class defines the OrderAggregate aggregate which aggregates the state of an
 * {@link Order} domain object.
 */
@Entity(name = "orders")
public class OrderAggregate extends ReflectiveMutableCommandProcessingAggregate<OrderAggregate, OrderCommand> {

    @Transient
    private final Log log = LogFactory.getLog(OrderAggregate.class);

    @Id
    private String id;

    private String orderNumber;


    protected OrderStatus status;
    private String version;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public List<Event> process(CreateOrderCommand cmd) {
        log.debug("Process CreateOrderCommand...");
        return EventUtil.events(new OrderCreatedEvent(cmd.getInitialStatus(), UUID.randomUUID().toString()));
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
        orderNumber = event.getOrderNumber();
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


    public String getVersion() {
        return version;
    }
}