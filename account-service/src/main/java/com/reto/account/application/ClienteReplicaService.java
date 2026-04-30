package com.reto.account.application;

import com.reto.account.domain.ClienteReplica;
import com.reto.account.domain.ClienteReplicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteReplicaService {

    private final ClienteReplicaRepository repository;

    public ClienteReplicaService(ClienteReplicaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void sync(ClienteChangedEvent event) {
        ClienteReplica replica = repository.findByClienteId(event.clienteId())
                .map(existing -> {
                    existing.refresh(event.nombre(), event.estado());
                    return existing;
                })
                .orElseGet(() -> new ClienteReplica(event.clienteId(), event.nombre(), event.estado()));

        repository.save(replica);
    }
}
