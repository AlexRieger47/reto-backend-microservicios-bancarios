package com.reto.account.application;

import com.reto.account.domain.Cuenta;
import java.math.BigDecimal;

public record CuentaResponse(
        Long id,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Boolean estado,
        String clienteId,
        String clienteNombre
) {

    public static CuentaResponse from(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getEstado(),
                cuenta.getClienteId(),
                cuenta.getClienteNombre()
        );
    }
}
