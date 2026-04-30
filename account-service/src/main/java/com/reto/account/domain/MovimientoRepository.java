package com.reto.account.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaClienteIdAndFechaBetweenOrderByFechaAsc(
            String clienteId,
            LocalDate startDate,
            LocalDate endDate
    );
}
