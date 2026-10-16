package com.segurosbolivar.polizas.controller;

import com.segurosbolivar.polizas.model.EstadoPoliza;
import com.segurosbolivar.polizas.model.Poliza;
import com.segurosbolivar.polizas.model.Riesgo;
import com.segurosbolivar.polizas.model.TipoPoliza;
import com.segurosbolivar.polizas.service.PolizaService;
import com.segurosbolivar.polizas.service.RiesgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polizas")
@RequiredArgsConstructor
public class PolizaController {

    private final PolizaService polizaService;
    private final RiesgoService riesgoService;

    @GetMapping
    public List<Poliza> listar(@RequestParam TipoPoliza tipo,
                               @RequestParam EstadoPoliza estado) {
        return polizaService.listar(tipo, estado);
    }

    @GetMapping("/{id}/riesgos")
    public List<Riesgo> listarRiesgos(@PathVariable Long id) {
        return riesgoService.listarPorPoliza(id);
    }

    @PostMapping("/{id}/renovar")
    public ResponseEntity<Poliza> renovar(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.renovar(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Poliza> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.cancelar(id));
    }

    @PostMapping("/{id}/riesgos")
    public ResponseEntity<Riesgo> agregarRiesgo(@PathVariable Long id,
                                                 @RequestBody Riesgo riesgo) {
        return ResponseEntity.ok(riesgoService.agregar(id, riesgo));
    }
}