package com.segurosbolivar.polizas.controller;

import com.segurosbolivar.polizas.model.Riesgo;
import com.segurosbolivar.polizas.service.RiesgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riesgos")
@RequiredArgsConstructor
public class RiesgoController {

    private final RiesgoService riesgoService;

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Riesgo> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(riesgoService.cancelar(id));
    }
}