package com.example.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderByOrderNumber(@Param("orderNumber") String orderNumber);
}
