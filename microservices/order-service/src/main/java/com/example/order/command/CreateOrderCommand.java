package com.example.order.command;

import com.example.order.domain.OrderStatus;

/**
 * Created by kbastani on 9/20/16.
 */
public class CreateOrderCommand implements OrderCommand {

    private OrderStatus initialStatus;

    public CreateOrderCommand(OrderStatus initialStatus) {
        this.initialStatus = initialStatus;
    }

    public OrderStatus getInitialStatus() {
        return initialStatus;
    }

}
