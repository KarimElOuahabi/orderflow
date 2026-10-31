package com.orderflow.order_service.application.port.out;

import com.orderflow.order_service.domain.Order;

import java.util.List;
import java.util.UUID;

public interface OutboxPort {

    void saveOrderCreated(Order order);

    List<PendingEvent> findPending();

    void markPublished(UUID eventId);
}