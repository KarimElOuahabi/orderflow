package com.orderflow.order_service.application;

import com.orderflow.order_service.application.port.out.OrderEventPublisher;
import com.orderflow.order_service.application.port.out.OutboxPort;
import com.orderflow.order_service.application.port.out.PendingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxRelayTest {

    @Mock
    OutboxPort outbox;

    @Mock
    OrderEventPublisher publisher;

    @InjectMocks
    OutboxRelay relay;

    @Test
    void relay_publishesPendingEvent_andMarksItPublished() {
        UUID id = UUID.randomUUID();
        when(outbox.findPending()).thenReturn(List.of(new PendingEvent(id, "OrderCreated", "{}")));

        relay.relay();

        verify(publisher).publish("OrderCreated", "{}");
        verify(outbox).markPublished(id);
    }

    @Test
    void relay_whenPublishFails_doesNotMarkEventPublished() {
        UUID id = UUID.randomUUID();
        when(outbox.findPending()).thenReturn(List.of(new PendingEvent(id, "OrderCreated", "{}")));
        doThrow(new IllegalStateException("AWS down")).when(publisher).publish(any(), any());

        relay.relay();

        verify(outbox, never()).markPublished(any());
    }
}