package com.example.order.domain;

/**
 * Created by kbastani on 9/20/16.
 */
public class UpdateOrderRequest {
    private OrderStatus status;

    public UpdateOrderRequest() {
    }

    public UpdateOrderRequest(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
