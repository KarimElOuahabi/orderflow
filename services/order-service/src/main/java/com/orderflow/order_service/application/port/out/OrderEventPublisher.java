package com.orderflow.order_service.application.port.out;

import com.orderflow.order_service.domain.Order;

public interface OrderEventPublisher {
    void publishOrderCreated(Order order);
}
