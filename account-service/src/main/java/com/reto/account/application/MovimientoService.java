package com.reto.account.application;

import com.reto.account.domain.Cuenta;
import com.reto.account.domain.CuentaRepository;
import com.reto.account.domain.Movimiento;
import com.reto.account.domain.MovimientoRepository;
import com.reto.account.infrastructure.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimientoService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public MovimientoService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> findAll() {
        return movimientoRepository.findAll().stream()
                .map(MovimientoResponse::from)
                .toList();
    }

    @Transactional
    public MovimientoResponse create(MovimientoRequest request) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.numeroCuenta())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        Movimiento movimiento = cuenta.registrarMovimiento(request.valor());
        cuentaRepository.save(cuenta);
        return MovimientoResponse.from(movimiento);
    }

    @Transactional
    public void delete(Long id) {
        if (!movimientoRepository.existsById(id)) {
            throw new NotFoundException("Movimiento no encontrado");
        }
        movimientoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReporteCuentaResponse> report(String dateRange, String clienteId) {
        DateRange range = DateRange.parse(dateRange);
        return movimientoRepository.findByCuentaClienteIdAndFechaBetweenOrderByFechaAsc(
                        clienteId,
                        range.start(),
                        range.end()
                ).stream()
                .map(ReporteCuentaResponse::from)
                .toList();
    }

    private record DateRange(LocalDate start, LocalDate end) {

        static DateRange parse(String raw) {
            String[] parts = raw.split("[:,]");
            if (parts.length != 2) {
                throw new IllegalArgumentException("El rango de fecha debe usar formato yyyy-MM-dd,yyyy-MM-dd");
            }
            return new DateRange(LocalDate.parse(parts[0].trim()), LocalDate.parse(parts[1].trim()));
        }
    }
}
