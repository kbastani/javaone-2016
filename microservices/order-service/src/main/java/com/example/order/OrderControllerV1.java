package com.example.order;

import com.example.order.aggregate.OrderAggregate;
import com.example.order.domain.CreateOrderRequest;
import com.example.order.domain.UpdateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/v1")
public class OrderControllerV1 {

    private OrderServiceV1 orderService;

    @Autowired
    public OrderControllerV1(OrderServiceV1 orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "orders/{id}")
    public OrderAggregate getOrder(@PathVariable String id)
            throws ExecutionException, InterruptedException {
        // Get the order entity and aggregate
        return orderService.getOrder(id);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "orders/{id}")
    public OrderAggregate updateOrder(@PathVariable String id, @RequestBody UpdateOrderRequest updateOrderRequest)
            throws ExecutionException, InterruptedException {
        // Update the order status
        return orderService.updateOrder(id, updateOrderRequest);
    }

    @RequestMapping(method = RequestMethod.POST, path = "orders")
    public OrderAggregate createOrder(@RequestBody CreateOrderRequest createOrderRequest)
            throws ExecutionException, InterruptedException {
        // Create new order entity and aggregate
        return orderService.createOrder(createOrderRequest);
    }
}
