package com.orderflow.order_service.application;

import com.orderflow.order_service.domain.Order;

public record OrderCreatedEvent(Order order) {
}
