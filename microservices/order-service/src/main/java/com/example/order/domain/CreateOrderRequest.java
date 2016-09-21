package com.example.order.domain;

public class CreateOrderRequest {

    private OrderStatus orderStatus;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "orderStatus=" + orderStatus +
                '}';
    }
}
