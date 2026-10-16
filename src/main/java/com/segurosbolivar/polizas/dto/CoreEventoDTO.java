package com.segurosbolivar.polizas.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CoreEventoDTO {
    private String evento;
    private Long polizaId;
}