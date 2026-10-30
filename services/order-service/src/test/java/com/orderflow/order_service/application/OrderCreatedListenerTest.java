package com.orderflow.order_service.application;

import com.orderflow.order_service.application.port.out.OrderEventPublisher;
import com.orderflow.order_service.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedListenerTest {

    @Mock
    OrderEventPublisher eventPublisher;

    @InjectMocks
    OrderCreatedListener listener;

    @Test
    void onOrderCreated_passesOrderToPublisher() {
        Order order = Order.create("c-01", new BigDecimal("100.00"));

        listener.onOrderCreated(new OrderCreatedEvent(order));

        verify(eventPublisher).publishOrderCreated(order);
        System.out.println("3 - OrderCreatedEvent passed to OrderEventPublisher successfully");
    }
}