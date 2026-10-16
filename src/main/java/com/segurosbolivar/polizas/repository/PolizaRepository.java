package com.segurosbolivar.polizas.repository;

import com.segurosbolivar.polizas.model.EstadoPoliza;
import com.segurosbolivar.polizas.model.Poliza;
import com.segurosbolivar.polizas.model.TipoPoliza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolizaRepository extends JpaRepository<Poliza, Long> {
    List<Poliza> findByTipoAndEstado(TipoPoliza tipo, EstadoPoliza estado);
}