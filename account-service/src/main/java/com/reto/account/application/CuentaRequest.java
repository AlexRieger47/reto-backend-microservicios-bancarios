package com.reto.account.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record CuentaRequest(
        @NotBlank(message = "El numero de cuenta es obligatorio")
        String numeroCuenta,

        @NotBlank(message = "El tipo de cuenta es obligatorio")
        String tipoCuenta,

        @NotNull(message = "El saldo inicial es obligatorio")
        @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
        BigDecimal saldoInicial,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado,

        @NotBlank(message = "El clienteId es obligatorio")
        String clienteId,

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String clienteNombre
) {
}
