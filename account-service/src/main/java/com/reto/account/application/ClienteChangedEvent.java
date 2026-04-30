package com.reto.account.application;

public record ClienteChangedEvent(
        String clienteId,
        String nombre,
        Boolean estado
) {
}
