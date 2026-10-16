package com.segurosbolivar.polizas.service;

import com.segurosbolivar.polizas.dto.CoreEventoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CoreClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${core.mock.url:http://localhost:8080/core-mock}")
    private String coreUrl;

    public void enviarEvento(Long polizaId, String evento) {
        CoreEventoDTO dto = new CoreEventoDTO(evento, polizaId);
        log.info("Enviando evento al CORE: {}", dto);
        try {
            restTemplate.postForEntity(coreUrl + "/evento", dto, Void.class);
        } catch (Exception e) {
            log.error("Error enviando evento al CORE: {}", e.getMessage());
        }
    }
}