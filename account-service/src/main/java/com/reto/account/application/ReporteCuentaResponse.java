package com.reto.account.application;

import com.reto.account.domain.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ReporteCuentaResponse(
        LocalDate fecha,
        String cliente,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        Boolean estado,
        BigDecimal movimiento,
        BigDecimal saldoDisponible
) {

    public static ReporteCuentaResponse from(Movimiento movimiento) {
        var cuenta = movimiento.getCuenta();
        return new ReporteCuentaResponse(
                movimiento.getFecha(),
                cuenta.getClienteNombre(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getEstado(),
                movimiento.getValor(),
                movimiento.getSaldo()
        );
    }
}
