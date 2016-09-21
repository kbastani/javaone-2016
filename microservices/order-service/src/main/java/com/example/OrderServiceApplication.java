package com.example;

import com.example.order.aggregate.OrderAggregate;
import com.example.order.command.OrderCommand;
import com.example.order.query.OrderQuery;
import io.eventuate.AggregateRepository;
import io.eventuate.EventuateAggregateStore;
import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.local.java.jdbckafkastore.EventuateJdbcEventStoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEventHandlers
@Import(EventuateJdbcEventStoreConfiguration.class)
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    protected OrderQuery orderQuery() {
        return new OrderQuery();
    }

    @Bean
    public AggregateRepository<OrderAggregate, OrderCommand>
    orderAggregateRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(OrderAggregate.class, eventStore);
    }
}
