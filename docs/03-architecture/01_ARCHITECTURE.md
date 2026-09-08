# Arquitectura

## Principios
- Proyecto independiente de VAEF.
- Reutiliza el generador backend y estándares del workspace VELTIA/VAEF.
- Backend modular.
- Mobile independiente.
- Motor adaptativo desacoplado.
- Persistencia relacional normalizada.
- Parametrización explícita.
- Auditoría de decisiones.
- IA detrás de un puerto/estrategia.

## Backend objetivo

```text
backend/
├── pom.xml
├── adaptive-quiz-domain/
├── adaptive-quiz-application/
├── adaptive-quiz-infrastructure/
└── adaptive-quiz-api/
```

### Domain
Entidades, value objects, contratos de repositorio, reglas de dominio.

### Application
Casos de uso, comandos/queries, servicios de aplicación.

### Infrastructure
JPA, repositorios, mappers, integración de BD y futuros proveedores IA.

### API
REST, DTO, validación, OpenAPI, configuración Spring.

## Regla de dependencia

```text
api -> application -> domain
          ^
          |
infrastructure
```

El dominio no depende de Spring, JPA, Android ni IA.
