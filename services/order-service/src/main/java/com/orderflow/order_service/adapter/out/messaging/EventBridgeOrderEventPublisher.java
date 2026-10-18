package com.orderflow.order_service.adapter.out.messaging;

import com.orderflow.order_service.application.port.out.OrderEventPublisher;
import com.orderflow.order_service.domain.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

@Component
public class EventBridgeOrderEventPublisher implements OrderEventPublisher {

    // These two values must match the EventBridge rule we wrote in Terraform,
    // otherwise the rule ignores the event and nothing reaches the queue.
    private static final String SOURCE = "orderflow.order-service";
    private static final String DETAIL_TYPE = "OrderCreated";

    private final EventBridgeClient eventBridge;
    private final String busName;

    public EventBridgeOrderEventPublisher(
            EventBridgeClient eventBridge,
            @Value("${orderflow.eventbridge.bus-name:orderflow-orders-bus}") String busName) {
        this.eventBridge = eventBridge;
        this.busName = busName;
    }

    @Override
    public void publishOrderCreated(Order order) {
        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .eventBusName(busName)
                .source(SOURCE)
                .detailType(DETAIL_TYPE)
                .detail(toJson(order))
                .build();

        PutEventsResponse response = eventBridge.putEvents(
                PutEventsRequest.builder().entries(entry).build());

        // putEvents does not throw when an entry is rejected, so we must check.
        if (response.failedEntryCount() > 0) {
            throw new IllegalStateException("EventBridge rejected the OrderCreated event: "
                    + response.entries().get(0).errorMessage());
        }
    }

    // Built by hand so we don't depend on which Jackson version Spring Boot ships.
    // customerId is the only free-text field, so it is the only one we escape.
    private String toJson(Order order) {
        return """
                {"orderId":"%s","customerId":"%s","totalAmount":%s,"status":"%s","createdAt":"%s"}"""
                .formatted(order.getId(), escape(order.getCustomerId()),
                        order.getTotalAmount().toPlainString(),
                        order.getStatus(), order.getCreatedAt());
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}