# ADR-0006 — Bootstrap PostgreSQL independiente con patrón VAEF

**Estado:** Aceptado

## Contexto

ADR-0002 autoriza reutilizar el generador VAEF como herramienta. La auditoría encontró que el generador vigente consulta metadatos MySQL mediante 'pymysql' y emite artefactos orientados a dicho motor. AdaptiveQuiz tiene PostgreSQL como fuente contractual y no debe adquirir una dependencia funcional de VAEF.

## Decisión

Se adopta la estructura y convenciones del generador VAEF, pero el bootstrap se implementa en el namespace independiente 'com.veltia.adaptivequiz':

- reactor Maven de cuatro módulos: domain, application, infrastructure y API;
- puertos de repositorio, adaptadores JPA y mappers separados;
- Flyway integrado desde los scripts canónicos de 'database/';
- PostgreSQL y 'ddl-auto=validate';
- OpenAPI, Actuator, validación y manejo uniforme de errores.

No se modifica el generador VAEF ni se lo ejecuta contra PostgreSQL hasta que cuente con soporte explícito para ese motor.

## Consecuencias

- AdaptiveQuiz conserva independencia de paquetes, datos y servicios VAEF.
- La migración se mantiene en una sola fuente: 'database/'. El empaquetado Maven la sincroniza hacia el classpath de Flyway durante el build, sin copias fuente divergentes.
- Una futura extensión del generador deberá incorporar PostgreSQL como capacidad propia y ser auditada antes de sustituir este bootstrap.

