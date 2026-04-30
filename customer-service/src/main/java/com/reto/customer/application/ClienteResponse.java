package com.reto.customer.application;

import com.reto.customer.domain.Cliente;

public record ClienteResponse(
        Long id,
        String clienteId,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        Boolean estado
) {

    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getEstado()
        );
    }
}
