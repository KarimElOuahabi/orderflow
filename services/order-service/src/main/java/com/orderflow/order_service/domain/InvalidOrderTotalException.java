package com.orderflow.order_service.domain;

public class InvalidOrderTotalException extends InvalidOrderException {
    public InvalidOrderTotalException(String message) {
        super(message);
    }
}
