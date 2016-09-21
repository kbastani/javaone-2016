package com.example.order.event;

import com.example.order.domain.OrderStatus;

public class OrderCreatedEvent implements OrderEvent {

    private OrderStatus initialStatus;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(OrderStatus initialStatus) {
        this.initialStatus = initialStatus;
    }

    public OrderStatus getInitialStatus() {
        return initialStatus;
    }
}
