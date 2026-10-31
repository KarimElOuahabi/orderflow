package com.orderflow.order_service.application;

import com.orderflow.order_service.application.port.out.OrderEventPublisher;
import com.orderflow.order_service.application.port.out.OutboxPort;
import com.orderflow.order_service.application.port.out.PendingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxPort outbox;
    private final OrderEventPublisher publisher;

    public OutboxRelay(OutboxPort outbox, OrderEventPublisher publisher) {
        this.outbox = outbox;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 5000)
    public void relay() {
        for (PendingEvent event : outbox.findPending()) {
            try {
                publisher.publish(event.eventType(), event.payload());
                outbox.markPublished(event.id());
                log.info("Outbox event {} published", event.id());
            } catch (Exception e) {
                log.warn("Outbox event {} not published, will retry: {}", event.id(), e.getMessage());
                return;
            }
        }
    }
}