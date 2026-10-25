package com.orderflow.order_service.domain;

public class InvalidCustomerIdException extends InvalidOrderException {
    public InvalidCustomerIdException(String message) {
        super(message);
    }
}
