package com.reto.account.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.account.application.CuentaRequest;
import com.reto.account.application.MovimientoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class MovimientoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createsMovementAndRejectsInsufficientBalance() throws Exception {
        CuentaRequest cuenta = new CuentaRequest(
                "225487",
                "Corriente",
                java.math.BigDecimal.valueOf(100),
                true,
                "CLI-002",
                "Marianela Montalvo"
        );

        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta)))
                .andExpect(status().isCreated());

        MvcResult createdMovement = mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MovimientoRequest("225487", java.math.BigDecimal.valueOf(50)))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldo").value(150))
                .andReturn();

        long movimientoId = objectMapper.readTree(createdMovement.getResponse().getContentAsString())
                .get("id")
                .asLong();

        mockMvc.perform(get("/movimientos/{id}", movimientoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movimientoId))
                .andExpect(jsonPath("$.numeroCuenta").value("225487"));

        mockMvc.perform(put("/movimientos/{id}", movimientoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MovimientoRequest("225487", java.math.BigDecimal.valueOf(80)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(80))
                .andExpect(jsonPath("$.saldo").value(180));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MovimientoRequest("225487", java.math.BigDecimal.valueOf(-300)))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));

        mockMvc.perform(get("/reportes")
                        .param("fecha", "2026-01-01,2026-12-31")
                .param("cliente", "CLI-002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cliente").value("Marianela Montalvo"))
                .andExpect(jsonPath("$[0].saldoDisponible").value(180));
    }
}
