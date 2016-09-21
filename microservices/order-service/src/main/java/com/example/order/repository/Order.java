package com.example.order.repository;

import com.example.order.aggregate.OrderAggregate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "orders")
public class Order extends OrderAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String entityId;
    private String orderNumber;

    public Order() {
        super();
        orderNumber = UUID.randomUUID().toString();
    }

    public Order(String entityId) {
        this();
        this.entityId = entityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Order withAggregate(OrderAggregate aggregate) {
        this.status = aggregate.getStatus();
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }
}
