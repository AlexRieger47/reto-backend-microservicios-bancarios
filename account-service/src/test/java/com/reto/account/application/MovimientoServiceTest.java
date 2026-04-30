package com.reto.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reto.account.domain.Cuenta;
import com.reto.account.domain.CuentaRepository;
import com.reto.account.domain.InsufficientBalanceException;
import com.reto.account.domain.MovimientoRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MovimientoServiceTest {

    private final CuentaRepository cuentaRepository = Mockito.mock(CuentaRepository.class);
    private final MovimientoRepository movimientoRepository = Mockito.mock(MovimientoRepository.class);
    private final MovimientoService service = new MovimientoService(cuentaRepository, movimientoRepository);

    @Test
    void registersDepositAndUpdatesBalance() {
        Cuenta cuenta = new Cuenta("478758", "Ahorros", BigDecimal.valueOf(1000), true, "CLI-001", "Jose Lema");
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));

        MovimientoResponse response = service.create(new MovimientoRequest("478758", BigDecimal.valueOf(500)));

        assertThat(response.tipoMovimiento()).isEqualTo("Deposito");
        assertThat(response.saldo()).isEqualByComparingTo("1500");
        assertThat(cuenta.getSaldoDisponible()).isEqualByComparingTo("1500");
        verify(cuentaRepository).save(cuenta);
    }

    @Test
    void rejectsWithdrawalWithoutBalance() {
        Cuenta cuenta = new Cuenta("478758", "Ahorros", BigDecimal.valueOf(100), true, "CLI-001", "Jose Lema");
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));

        assertThatThrownBy(() -> service.create(new MovimientoRequest("478758", BigDecimal.valueOf(-300))))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessage("Saldo no disponible");
    }
}
