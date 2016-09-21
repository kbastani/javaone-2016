package com.example.order.repository;

import com.example.order.aggregate.OrderAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderAggregate, Long> {
    OrderAggregate findOrderByOrderNumber(@Param("orderNumber") String orderNumber);
}
