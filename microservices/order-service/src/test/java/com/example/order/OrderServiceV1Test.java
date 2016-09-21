package com.example.order;

import com.example.OrderServiceApplication;
import com.example.order.aggregate.OrderAggregate;
import com.example.order.command.ConfirmOrderCommand;
import com.example.order.command.PendingOrderCommand;
import com.example.order.command.ShipOrderCommand;
import com.example.order.domain.CreateOrderRequest;
import com.example.order.domain.OrderStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;

import static io.eventuate.testutil.AsyncUtil.await;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class OrderServiceV1Test {

    @Autowired
    private OrderServiceV1 orderServiceV1;

    @Test
    public void createOrder() throws Exception {
        // Create a new order and aggregate
        OrderAggregate expected = orderServiceV1.createOrder(new CreateOrderRequest(OrderStatus.CREATED));
        Assert.assertEquals(expected.getStatus(), OrderStatus.CREATED);

        String orderNumber = expected.getOrderNumber();

        // Process the pending order command
        expected = orderServiceV1.process(orderNumber, new PendingOrderCommand());
        Assert.assertEquals(expected.getStatus(), OrderStatus.PENDING);

        // Process the confirm order command
        expected = orderServiceV1.process(orderNumber, new ConfirmOrderCommand());
        Assert.assertEquals(expected.getStatus(), OrderStatus.CONFIRMED);

        // Process the confirm order command
        expected = orderServiceV1.process(orderNumber, new ShipOrderCommand());
        Assert.assertEquals(expected.getStatus(), OrderStatus.SHIPPED);

        // Lookup actual order and compare status
        OrderAggregate actual = orderServiceV1.getOrder(expected.getOrderNumber());
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }

}