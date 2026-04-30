package com.reto.account.api;

import com.reto.account.application.CuentaRequest;
import com.reto.account.application.CuentaResponse;
import com.reto.account.application.CuentaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService service;

    public CuentaController(CuentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CuentaResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CuentaResponse findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> create(@Valid @RequestBody CuentaRequest request) {
        CuentaResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/cuentas/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public CuentaResponse update(@PathVariable("id") Long id, @Valid @RequestBody CuentaRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
