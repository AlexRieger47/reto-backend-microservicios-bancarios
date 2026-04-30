package com.reto.customer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void createsClienteWithPersonaData() {
        Cliente cliente = new Cliente(
                "CLI-001",
                "Jose Lema",
                "Masculino",
                32,
                "0102030405",
                "Otavalo sn y principal",
                "0987654321",
                "1234",
                true
        );

        assertThat(cliente.getClienteId()).isEqualTo("CLI-001");
        assertThat(cliente.getNombre()).isEqualTo("Jose Lema");
        assertThat(cliente.getEstado()).isTrue();
    }
}
