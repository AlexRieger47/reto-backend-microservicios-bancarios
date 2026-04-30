package com.reto.account.api;

import com.reto.account.application.MovimientoService;
import com.reto.account.application.ReporteCuentaResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReporteController {

    private final MovimientoService service;

    public ReporteController(MovimientoService service) {
        this.service = service;
    }

    @GetMapping("/reportes")
    public List<ReporteCuentaResponse> report(
            @RequestParam("fecha") String dateRange,
            @RequestParam("cliente") String clienteId
    ) {
        return service.report(dateRange, clienteId);
    }
}
