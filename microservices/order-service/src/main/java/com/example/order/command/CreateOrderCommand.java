package com.example.order.command;

import com.example.order.repository.Order;
import com.example.order.domain.OrderStatus;

/**
 * Created by kbastani on 9/20/16.
 */
public class CreateOrderCommand implements OrderCommand {

    private OrderStatus initialStatus;
    private Order orderEntity;

    public CreateOrderCommand(OrderStatus initialStatus) {
        this.orderEntity = new Order();
        this.initialStatus = initialStatus;
    }

    public OrderStatus getInitialStatus() {
        return initialStatus;
    }

    public Order getOrderEntity() {
        return orderEntity;
    }
}
