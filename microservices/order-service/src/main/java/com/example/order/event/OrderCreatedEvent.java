package com.example.order.event;

import com.example.order.domain.OrderStatus;

public class OrderCreatedEvent implements OrderEvent {

    private OrderStatus initialStatus;
    private String orderNumber;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(OrderStatus initialStatus, String orderNumber) {
        this.initialStatus = initialStatus;
        this.orderNumber = orderNumber;
    }

    public OrderStatus getInitialStatus() {
        return initialStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
