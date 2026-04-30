package com.reto.account.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MovimientoRequest(
        @NotBlank(message = "El numero de cuenta es obligatorio")
        String numeroCuenta,

        @NotNull(message = "El valor es obligatorio")
        BigDecimal valor
) {
}
