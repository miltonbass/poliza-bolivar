# API de Gestión de Pólizas – Prueba Técnica

API REST para gestión de pólizas de arrendamiento (individuales y colectivas), con renovación con IPC, cancelación en cascada e integración con CORE legado.

## Stack

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- H2 (base de datos en memoria)
- Lombok
- Maven

## Cómo ejecutar

```bash
git clone https://github.com/miltonbass/poliza-bolivar.git
cd poliza-bolivar
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Autenticación

Todas las peticiones requieren el header:

```
x-api-key: 123456
```

Excepciones: `/core-mock/**` y `/h2-console/**`.

## Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/polizas?tipo=COLECTIVA&estado=ACTIVA` | Listar pólizas por tipo y estado |
| GET | `/polizas/{id}/riesgos` | Listar riesgos de una póliza |
| POST | `/polizas/{id}/renovar` | Renovar póliza (+IPC) |
| POST | `/polizas/{id}/cancelar` | Cancelar póliza y todos sus riesgos |
| POST | `/polizas/{id}/riesgos` | Agregar riesgo (solo COLECTIVA) |
| POST | `/riesgos/{id}/cancelar` | Cancelar un riesgo |
| POST | `/core-mock/evento` | Mock del CORE legado |

## Ejemplos con cURL

```bash
# Listar pólizas colectivas activas
curl -H "x-api-key: 123456" \
  "http://localhost:8080/polizas?tipo=COLECTIVA&estado=ACTIVA"

# Renovar póliza
curl -X POST -H "x-api-key: 123456" \
  http://localhost:8080/polizas/1/renovar

# Cancelar póliza
curl -X POST -H "x-api-key: 123456" \
  http://localhost:8080/polizas/1/cancelar

# Agregar riesgo a póliza colectiva
curl -X POST -H "x-api-key: 123456" \
  -H "Content-Type: application/json" \
  -d '{"direccionInmueble":"Calle 123","arrendatarioId":"A1","canonMensual":1000000}' \
  http://localhost:8080/polizas/1/riesgos

# Cancelar riesgo
curl -X POST -H "x-api-key: 123456" \
  http://localhost:8080/riesgos/1/cancelar

# Mock del CORE (no requiere API Key)
curl -X POST -H "Content-Type: application/json" \
  -d '{"evento":"ACTUALIZACION","polizaId":555}' \
  http://localhost:8080/core-mock/evento
```

## Reglas de negocio implementadas

- Una póliza individual solo puede tener 1 riesgo.
- No se puede renovar una póliza cancelada.
- La cancelación de una póliza cancela todos sus riesgos (cascada).
- Agregar riesgo exige que la póliza sea COLECTIVA.
- Toda modificación (renovar, cancelar, agregar/cancelar riesgo) envía evento al CORE.
- La renovación aplica el IPC al canon y recalcula la prima.

## Arquitectura

- **Controller → Service → Repository** (arquitectura en capas).
- **Manejo centralizado de errores** con `@RestControllerAdvice`.
- **Filtro de API Key** como componente transversal.
- **Cliente del CORE** desacoplado en `CoreClientService`.

## Estructura

```
src/main/java/com/segurosbolivar/polizas/
├── PolizasApplication.java
├── config/        → ApiKeyFilter
├── controller/    → PolizaController, RiesgoController, CoreMockController
├── service/       → PolizaService, RiesgoService, CoreClientService
├── repository/    → PolizaRepository, RiesgoRepository
├── model/         → Poliza, Riesgo, TipoPoliza, EstadoPoliza
├── dto/           → CoreEventoDTO
└── exception/     → BusinessException, GlobalExceptionHandler
```

## Autor

Milton Triviño Fuquen