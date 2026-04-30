package com.reto.account.application;

import com.reto.account.domain.Cuenta;
import com.reto.account.domain.CuentaRepository;
import com.reto.account.infrastructure.ConflictException;
import com.reto.account.infrastructure.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CuentaService {

    private final CuentaRepository repository;

    public CuentaService(CuentaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CuentaResponse> findAll() {
        return repository.findAll().stream()
                .map(CuentaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CuentaResponse findById(Long id) {
        return CuentaResponse.from(findEntity(id));
    }

    @Transactional
    public CuentaResponse create(CuentaRequest request) {
        if (repository.existsByNumeroCuenta(request.numeroCuenta())) {
            throw new ConflictException("El numero de cuenta ya existe");
        }

        Cuenta cuenta = new Cuenta(
                request.numeroCuenta(),
                request.tipoCuenta(),
                request.saldoInicial(),
                request.estado(),
                request.clienteId(),
                request.clienteNombre()
        );

        return CuentaResponse.from(repository.save(cuenta));
    }

    @Transactional
    public CuentaResponse update(Long id, CuentaRequest request) {
        Cuenta cuenta = findEntity(id);
        repository.findByNumeroCuenta(request.numeroCuenta())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("El numero de cuenta ya existe");
                });

        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldoInicial(request.saldoInicial());
        cuenta.setEstado(request.estado());
        cuenta.setClienteId(request.clienteId());
        cuenta.setClienteNombre(request.clienteNombre());
        return CuentaResponse.from(repository.save(cuenta));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findEntity(id));
    }

    private Cuenta findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
    }
}
