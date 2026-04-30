package com.reto.account.infrastructure;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
