package com.example.order;

import com.example.OrderServiceApplication;
import com.example.order.command.ConfirmOrderCommand;
import com.example.order.command.PendingOrderCommand;
import com.example.order.command.ShipOrderCommand;
import com.example.order.domain.CreateOrderRequest;
import com.example.order.domain.OrderStatus;
import com.example.order.repository.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class OrderServiceV1Test {

    @Autowired
    private OrderServiceV1 orderServiceV1;

    @Test
    public void createOrder() throws Exception {
        // Create a new order and aggregate
        Order expected = orderServiceV1.createOrder(new CreateOrderRequest(OrderStatus.CREATED));
        Assert.assertEquals(expected.getStatus(), OrderStatus.CREATED);

        // Process the pending order command
        expected = orderServiceV1.process(expected, new PendingOrderCommand());
        Assert.assertEquals(expected.getStatus(), OrderStatus.PENDING);

        // Process the confirm order command
        expected = orderServiceV1.process(expected, new ConfirmOrderCommand());
        Assert.assertEquals(expected.getStatus(), OrderStatus.CONFIRMED);

        // Process the confirm order command
        expected = orderServiceV1.process(expected, new ShipOrderCommand());
        Assert.assertEquals(expected.getStatus(), OrderStatus.SHIPPED);

        // Lookup actual order and compare status
        Order actual = orderServiceV1.getOrder(expected.getOrderNumber());
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }

}