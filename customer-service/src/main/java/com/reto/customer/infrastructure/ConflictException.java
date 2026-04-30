package com.reto.customer.infrastructure;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
