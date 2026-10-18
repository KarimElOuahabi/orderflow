package com.orderflow.order_service.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final String customerId;
    private final BigDecimal totalAmount;
    private OrderStatus status;
    private final Instant createdAt;

    public Order(UUID id, String customerId, BigDecimal totalAmount,
                 OrderStatus status, Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Order create(String customerId, BigDecimal totalAmount) {
        if (totalAmount == null || totalAmount.signum() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
        return new Order(UUID.randomUUID(), customerId, totalAmount,
                OrderStatus.CREATED, Instant.now());
    }

    public UUID getId() { return id; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}