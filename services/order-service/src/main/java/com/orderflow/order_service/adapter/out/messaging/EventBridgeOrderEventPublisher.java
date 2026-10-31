package com.orderflow.order_service.adapter.out.messaging;

import com.orderflow.order_service.application.port.out.OrderEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

@Component
public class EventBridgeOrderEventPublisher implements OrderEventPublisher {

    // Must match the EventBridge rule in Terraform, otherwise the rule ignores the event.
    private static final String SOURCE = "orderflow.order-service";

    private final EventBridgeClient eventBridge;
    private final String busName;

    public EventBridgeOrderEventPublisher(
            EventBridgeClient eventBridge,
            @Value("${orderflow.eventbridge.bus-name:orderflow-orders-bus}") String busName) {
        this.eventBridge = eventBridge;
        this.busName = busName;
    }

    @Override
    public void publish(String eventType, String payload) {
        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .eventBusName(busName)
                .source(SOURCE)
                .detailType(eventType)
                .detail(payload)
                .build();

        PutEventsResponse response = eventBridge.putEvents(
                PutEventsRequest.builder().entries(entry).build());

        // putEvents does not throw when an entry is rejected, so we must check.
        if (response.failedEntryCount() > 0) {
            throw new IllegalStateException("EventBridge rejected the " + eventType + " event: "
                    + response.entries().get(0).errorMessage());
        }
    }
}