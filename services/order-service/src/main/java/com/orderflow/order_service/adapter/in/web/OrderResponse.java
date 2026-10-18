package com.orderflow.order_service.adapter.in.web;

import com.orderflow.order_service.domain.Order;
import com.orderflow.order_service.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(UUID id, String customerId, BigDecimal totalAmount,
                            OrderStatus status, Instant createdAt) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerId(),
                order.getTotalAmount(), order.getStatus(), order.getCreatedAt());
    }
}