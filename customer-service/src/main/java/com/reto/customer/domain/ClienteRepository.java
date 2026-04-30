package com.reto.customer.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByClienteId(String clienteId);

    boolean existsByClienteId(String clienteId);
}
