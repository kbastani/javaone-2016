package com.example.order.event;

import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity="com.example.order.aggregate.OrderAggregate")
public interface OrderEvent extends Event {
}
