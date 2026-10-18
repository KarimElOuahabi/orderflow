package com.orderflow.order_service.application;

import com.orderflow.order_service.application.port.out.OrderEventPublisher;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public class OrderCreatedListener {
    private final OrderEventPublisher eventPublisher;

    public OrderCreatedListener(OrderEventPublisher evenPublisher) {
        this.eventPublisher = evenPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        eventPublisher.publishOrderCreated(event.order());
    }
}
