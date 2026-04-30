package com.reto.account.api;

import com.reto.account.application.MovimientoRequest;
import com.reto.account.application.MovimientoResponse;
import com.reto.account.application.MovimientoService;
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
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService service;

    public MovimientoController(MovimientoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovimientoResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MovimientoResponse findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> create(@Valid @RequestBody MovimientoRequest request) {
        MovimientoResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/movimientos/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public MovimientoResponse update(@PathVariable("id") Long id, @Valid @RequestBody MovimientoRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
