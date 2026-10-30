package com.orderflow.order_service.application;

import com.orderflow.order_service.application.port.out.OrderRepository;
import com.orderflow.order_service.domain.InvalidOrderTotalException;
import com.orderflow.order_service.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ApplicationEventPublisher applicationEvents;

    @InjectMocks
    OrderService orderService;

    @Test
    void createOrder_savesOrder_andPublishesEvent() {
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.createOrder("c-01", new BigDecimal("100.00"));

        assertEquals("c-01", result.getCustomerId());
        verify(orderRepository).save(any(Order.class));
        System.out.println("1 - Order saved successfully");
        verify(applicationEvents).publishEvent(any(OrderCreatedEvent.class));
        System.out.println("2 - OrderCreatedEvent published successfully");
    }

    @Test
    void createOrder_invalidTotal_savesNothing_andPublishesNothing() {
        assertThrows(InvalidOrderTotalException.class,
                () -> orderService.createOrder("c-01", new BigDecimal("-5")));

        verifyNoInteractions(orderRepository, applicationEvents);
        System.out.println("4 - Invalid order rejected: nothing saved, nothing published");
    }
}