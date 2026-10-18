package com.orderflow.order_service.application.port.out;

import com.orderflow.order_service.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID id);
}
