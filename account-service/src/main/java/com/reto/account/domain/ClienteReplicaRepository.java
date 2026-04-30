package com.reto.account.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteReplicaRepository extends JpaRepository<ClienteReplica, Long> {

    Optional<ClienteReplica> findByClienteId(String clienteId);
}
