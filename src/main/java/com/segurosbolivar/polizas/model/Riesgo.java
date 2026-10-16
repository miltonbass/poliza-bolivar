package com.segurosbolivar.polizas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "riesgo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliza_id", nullable = false)
    @JsonIgnore                    // ← AGREGA ESTA LÍNEA
    private Poliza poliza;

    private String direccionInmueble;
    private String arrendatarioId;

    @Column(precision = 19, scale = 2)
    private BigDecimal canonMensual;

    @Enumerated(EnumType.STRING)
    private EstadoRiesgo estado;

    public enum EstadoRiesgo {
        ACTIVO,
        CANCELADO
    }
}