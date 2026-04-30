package com.reto.customer.application;

import com.reto.customer.domain.Cliente;
import com.reto.customer.domain.ClienteRepository;
import com.reto.customer.infrastructure.ConflictException;
import com.reto.customer.infrastructure.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final CustomerEventPublisher eventPublisher;

    public ClienteService(ClienteRepository repository, CustomerEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> findAll() {
        return repository.findAll().stream()
                .map(ClienteResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse findById(Long id) {
        return ClienteResponse.from(findEntity(id));
    }

    @Transactional
    public ClienteResponse create(ClienteRequest request) {
        if (repository.existsByClienteId(request.clienteId())) {
            throw new ConflictException("El clienteId ya existe");
        }

        Cliente cliente = new Cliente(
                request.clienteId(),
                request.nombre(),
                request.genero(),
                request.edad(),
                request.identificacion(),
                request.direccion(),
                request.telefono(),
                request.contrasena(),
                request.estado()
        );

        Cliente saved = repository.save(cliente);
        eventPublisher.publish(ClienteChangedEvent.from(saved));
        return ClienteResponse.from(saved);
    }

    @Transactional
    public ClienteResponse update(Long id, ClienteRequest request) {
        Cliente cliente = findEntity(id);
        repository.findByClienteId(request.clienteId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("El clienteId ya existe");
                });

        cliente.setClienteId(request.clienteId());
        cliente.setNombre(request.nombre());
        cliente.setGenero(request.genero());
        cliente.setEdad(request.edad());
        cliente.setIdentificacion(request.identificacion());
        cliente.setDireccion(request.direccion());
        cliente.setTelefono(request.telefono());
        cliente.setContrasena(request.contrasena());
        cliente.setEstado(request.estado());

        Cliente saved = repository.save(cliente);
        eventPublisher.publish(ClienteChangedEvent.from(saved));
        return ClienteResponse.from(saved);
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = findEntity(id);
        repository.delete(cliente);
    }

    private Cliente findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    }
}
