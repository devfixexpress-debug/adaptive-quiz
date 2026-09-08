# Changelog

## [Unreleased]

## [0.2.0] - 2026-09-08

### Added

- Flujo de práctica real: sesión, siguiente ejercicio, calificación, intento, respuesta y progreso.
- Motor adaptativo basado en reglas, con contexto de cinco intentos, análisis de rendimiento,
  persistencia de `ADP_CONTEXTO_APRENDIZAJE`, `ADP_EVENTO_ADAPTACION` y `ADP_ACCION_EVENTO`.
- Migración inmutable `V2__adaptive_quiz_mvp.sql` con parámetros de dificultad/tipo inicial y
  versión del motor.
- Endpoints de práctica, progreso e historial/detalle de adaptaciones.
- MVP Android Kotlin/Compose con Inicio, Práctica, Resultado, Monitor Adaptativo y Progreso;
  consume la API real mediante Retrofit y captura el tiempo de respuesta automáticamente.
- Evidencia de build Android y de validación directa con Spring Boot y PostgreSQL reales.

### Changed

- La certificación de esta iteración se realiza contra runtime real, sin introducir nuevas
  carpetas de pruebas ni mocks.

### Fixed

- Declarado android.permission.INTERNET para permitir que el APK debug consuma el backend local
  desde el emulador.
- Añadido icono vectorial de aplicación y actualizado targetSdk a 37.

## [0.1.0] - Foundation

### Added

- Estructura documental del Taller 001.
- Requisitos y trazabilidad inicial.
- Arquitectura de datos robusta y paramétrica.
- Modelo lógico, diccionario de datos y DDL PostgreSQL.
- Diseño del motor adaptativo basado en reglas.
- Roadmap de IA desacoplada.
- Guiones de sustentación y demostración.
- Backend Spring Boot modular e independiente con módulos domain, application, infrastructure y API.
- Integración Flyway con las migraciones y seed canónicos de PostgreSQL.
- Primer vertical slice REST: asignaturas, temas por asignatura y detalle de ejercicio.
- OpenAPI, Actuator, validación, manejo uniforme de errores y contratos base del motor adaptativo.
- Evidencia de auditoría, validación de base de datos y ejecución real local.
