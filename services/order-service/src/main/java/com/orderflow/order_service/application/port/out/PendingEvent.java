package com.orderflow.order_service.application.port.out;

import java.util.UUID;

public record PendingEvent(UUID id, String eventType, String payload) {
}