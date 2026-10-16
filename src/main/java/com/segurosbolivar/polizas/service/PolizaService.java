package com.segurosbolivar.polizas.service;

import com.segurosbolivar.polizas.exception.BusinessException;
import com.segurosbolivar.polizas.model.EstadoPoliza;
import com.segurosbolivar.polizas.model.Poliza;
import com.segurosbolivar.polizas.model.Riesgo;
import com.segurosbolivar.polizas.model.TipoPoliza;
import com.segurosbolivar.polizas.repository.PolizaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolizaService {

    private final PolizaRepository polizaRepository;
    private final CoreClientService coreClientService;

    @Value("${api.ipc:0.05}")
    private BigDecimal ipc;

    public List<Poliza> listar(TipoPoliza tipo, EstadoPoliza estado) {
        return polizaRepository.findByTipoAndEstado(tipo, estado);
    }

    @Transactional
    public Poliza renovar(Long id) {
        Poliza p = polizaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Póliza no encontrada"));

        if (p.getEstado() == EstadoPoliza.CANCELADA) {
            throw new BusinessException("No se puede renovar una póliza cancelada");
        }

        BigDecimal factor = BigDecimal.ONE.add(ipc);
        p.setCanonMensual(p.getCanonMensual().multiply(factor).setScale(2, RoundingMode.HALF_UP));
        p.setPrima(p.getCanonMensual().multiply(BigDecimal.valueOf(p.getMesesVigencia())));
        p.setEstado(EstadoPoliza.RENOVADA);
        p.setFechaInicio(p.getFechaFin());
        p.setFechaFin(p.getFechaFin().plusMonths(p.getMesesVigencia()));

        polizaRepository.save(p);
        coreClientService.enviarEvento(p.getId(), "ACTUALIZACION");
        return p;
    }

    @Transactional
    public Poliza cancelar(Long id) {
        Poliza p = polizaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Póliza no encontrada"));

        p.setEstado(EstadoPoliza.CANCELADA);
        p.getRiesgos().forEach(r -> r.setEstado(Riesgo.EstadoRiesgo.CANCELADO));

        polizaRepository.save(p);
        coreClientService.enviarEvento(p.getId(), "ACTUALIZACION");
        return p;
    }
}