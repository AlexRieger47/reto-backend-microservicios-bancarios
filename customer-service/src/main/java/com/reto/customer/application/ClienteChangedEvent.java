package com.reto.customer.application;

import com.reto.customer.domain.Cliente;

public record ClienteChangedEvent(
        String clienteId,
        String nombre,
        Boolean estado
) {

    public static ClienteChangedEvent from(Cliente cliente) {
        return new ClienteChangedEvent(cliente.getClienteId(), cliente.getNombre(), cliente.getEstado());
    }
}
