package com.segurosbolivar.polizas.service;

import com.segurosbolivar.polizas.exception.BusinessException;
import com.segurosbolivar.polizas.model.EstadoPoliza;
import com.segurosbolivar.polizas.model.Poliza;
import com.segurosbolivar.polizas.model.Riesgo;
import com.segurosbolivar.polizas.model.TipoPoliza;
import com.segurosbolivar.polizas.repository.PolizaRepository;
import com.segurosbolivar.polizas.repository.RiesgoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiesgoService {

    private final RiesgoRepository riesgoRepository;
    private final PolizaRepository polizaRepository;
    private final CoreClientService coreClientService;

    public List<Riesgo> listarPorPoliza(Long polizaId) {
        return riesgoRepository.findByPolizaId(polizaId);
    }

    @Transactional
    public Riesgo agregar(Long polizaId, Riesgo riesgo) {
        Poliza p = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new BusinessException("Póliza no encontrada"));

        if (p.getTipo() != TipoPoliza.COLECTIVA) {
            throw new BusinessException("Solo se pueden agregar riesgos a pólizas colectivas");
        }
        if (p.getEstado() == EstadoPoliza.CANCELADA) {
            throw new BusinessException("No se puede agregar riesgo a póliza cancelada");
        }

        riesgo.setPoliza(p);
        riesgo.setEstado(Riesgo.EstadoRiesgo.ACTIVO);
        Riesgo guardado = riesgoRepository.save(riesgo);

        coreClientService.enviarEvento(p.getId(), "ACTUALIZACION");
        return guardado;
    }

    @Transactional
    public Riesgo cancelar(Long riesgoId) {
        Riesgo r = riesgoRepository.findById(riesgoId)
                .orElseThrow(() -> new BusinessException("Riesgo no encontrado"));

        r.setEstado(Riesgo.EstadoRiesgo.CANCELADO);
        riesgoRepository.save(r);

        coreClientService.enviarEvento(r.getPoliza().getId(), "ACTUALIZACION");
        return r;
    }
}