package com.reto.customer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente extends Persona {

    @Column(name = "cliente_id", nullable = false, unique = true)
    private String clienteId;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private Boolean estado;

    protected Cliente() {
    }

    public Cliente(
            String clienteId,
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena,
            Boolean estado
    ) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
