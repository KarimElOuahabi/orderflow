package com.orderflow.order_service.application;

import com.orderflow.order_service.application.port.in.CreateOrderUseCase;
import com.orderflow.order_service.application.port.out.OrderRepository;
import com.orderflow.order_service.domain.Order;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService implements CreateOrderUseCase {
    private OrderRepository orderRepository;
    private ApplicationEventPublisher applicationEvents;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher applicationEvents) {
        this.orderRepository = orderRepository;
        this.applicationEvents = applicationEvents;
    }

    @Override
    @Transactional
    public Order createOrder(String customerId, BigDecimal orderAmount) {
        Order order = Order.create(customerId, orderAmount);
        Order saved = orderRepository.save(order);
        applicationEvents.publishEvent(new  OrderCreatedEvent(saved));
        return saved;
    }
}
