package com.example.order;

import com.example.order.domain.CreateOrderRequest;
import com.example.order.domain.UpdateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
    public ResponseEntity<?> getOrder(@PathVariable String id)
            throws ExecutionException, InterruptedException {
        // Get the order entity and aggregate
        return Optional.of(new ResponseEntity<>(orderService.getOrder(id), HttpStatus.OK))
                .map(a -> a)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody UpdateOrderRequest updateOrderRequest)
            throws ExecutionException, InterruptedException {
        // Update the order status
        return Optional.of(new ResponseEntity<>(orderService.updateOrder(id, updateOrderRequest),
                HttpStatus.OK))
                .map(a -> a)
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(method = RequestMethod.POST, path = "orders")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest)
            throws ExecutionException, InterruptedException {
        // Create new order entity and aggregate
        return Optional.of(new ResponseEntity<>(orderService.createOrder(createOrderRequest), HttpStatus.OK))
                .map(a -> a)
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
