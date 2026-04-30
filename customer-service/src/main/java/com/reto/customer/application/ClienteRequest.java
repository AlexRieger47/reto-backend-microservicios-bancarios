package com.reto.customer.application;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(
        @NotBlank(message = "El clienteId es obligatorio")
        String clienteId,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El genero es obligatorio")
        String genero,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 0, message = "La edad no puede ser negativa")
        Integer edad,

        @NotBlank(message = "La identificacion es obligatoria")
        String identificacion,

        @NotBlank(message = "La direccion es obligatoria")
        String direccion,

        @NotBlank(message = "El telefono es obligatorio")
        String telefono,

        @NotBlank(message = "La contrasena es obligatoria")
        String contrasena,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {
}
