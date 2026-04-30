package com.reto.account.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes_replica")
public class ClienteReplica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false, unique = true)
    private String clienteId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Boolean estado;

    protected ClienteReplica() {
    }

    public ClienteReplica(String clienteId, String nombre, Boolean estado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void refresh(String nombre, Boolean estado) {
        this.nombre = nombre;
        this.estado = estado;
    }
}
