package com.reto.account.application;

import com.reto.account.domain.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimientoResponse(
        Long id,
        LocalDate fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        String numeroCuenta
) {

    public static MovimientoResponse from(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.getCuenta().getNumeroCuenta()
        );
    }
}
