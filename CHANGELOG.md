# Changelog

## [Unreleased]

### Added

- Configuración adaptativa de uso docente para consultar, modificar de forma limitada y restaurar
  los umbrales persistidos de la política certificada, sin selector manual de dificultad.

### Changed

- Documentado el núcleo adaptativo y el procedimiento reproducible de cambio de parámetros en vivo.

## [1.0.0] - 2026-09-08

### Added

- Gradle Wrapper Android versionado para builds reproducibles fuera de Android Studio.
- Cobertura unitaria determinista del motor adaptativo y de la construcción de contexto,
  complementaria a la certificación E2E contra runtime real.
- Workflows GitHub Actions para validación independiente de backend y Android.

### Changed

- Documentación de release, contrato REST y comandos de ejecución alineados con la versión 1.0.0.
- La ventana adaptativa se describe como una ventana móvil de hasta cinco intentos recientes
  disponibles, según `tamano_ventana_intentos` de la política.

## [0.2.0] - 2026-09-08

### Added

- Flujo de práctica real: sesión, siguiente ejercicio, calificación, intento, respuesta y progreso.
- Motor adaptativo basado en reglas, con una ventana móvil de hasta cinco intentos recientes
  disponibles, según `tamano_ventana_intentos` de la política, análisis de rendimiento y
  persistencia de `ADP_CONTEXTO_APRENDIZAJE`, `ADP_EVENTO_ADAPTACION` y `ADP_ACCION_EVENTO`.
- Migración inmutable `V2__adaptive_quiz_mvp.sql` con parámetros de dificultad/tipo inicial y
  versión del motor.
- Endpoints de práctica, progreso e historial/detalle de adaptaciones.
- MVP Android Kotlin/Compose con Inicio, Práctica, Resultado, Monitor Adaptativo y Progreso;
  consume la API real mediante Retrofit y captura el tiempo de respuesta automáticamente.
- Evidencia de build Android y de validación directa con Spring Boot y PostgreSQL reales.

### Changed

- La certificación de la iteración se realizó contra runtime real; el release posterior la
  complementa con pruebas unitarias puras, sin sustituir esa evidencia por mocks.

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
