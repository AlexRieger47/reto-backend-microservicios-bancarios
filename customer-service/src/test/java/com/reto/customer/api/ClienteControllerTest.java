package com.reto.customer.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.customer.application.ClienteRequest;
import com.reto.customer.application.ClienteResponse;
import com.reto.customer.application.ClienteService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService service;

    @Test
    void listsClientes() throws Exception {
        when(service.findAll()).thenReturn(List.of(new ClienteResponse(
                1L,
                "CLI-001",
                "Jose Lema",
                "Masculino",
                32,
                "0102030405",
                "Otavalo sn y principal",
                "0987654321",
                true
        )));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value("CLI-001"));
    }

    @Test
    void createsCliente() throws Exception {
        ClienteRequest request = new ClienteRequest(
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

        when(service.create(any())).thenReturn(new ClienteResponse(
                1L,
                "CLI-001",
                "Jose Lema",
                "Masculino",
                32,
                "0102030405",
                "Otavalo sn y principal",
                "0987654321",
                true
        ));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value("CLI-001"));
    }
}
