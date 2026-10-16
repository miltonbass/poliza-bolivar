package com.segurosbolivar.polizas.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "poliza")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroPoliza;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPoliza tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPoliza estado;

    private String tomadorId;
    private String aseguradoId;
    private String beneficiarioId;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Column(precision = 19, scale = 2)
    private BigDecimal canonMensual;

    @Column(precision = 19, scale = 2)
    private BigDecimal prima;

    private Integer mesesVigencia;

    @OneToMany(mappedBy = "poliza", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Riesgo> riesgos = new ArrayList<>();
}