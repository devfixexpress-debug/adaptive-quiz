# Checklist de entrega

## Repositorio y reproducibilidad

- [x] Repositorio público/visible para docente y release `v1.0.0` publicada.
- [x] Tag `v1.0.0` existente e inmutable.
- [x] README actualizado con propósito, arquitectura, pasos de ejecución, demo, evidencias y alcance de IA.
- [x] `.env.example`, `docker-compose.yml`, Gradle Wrapper y Maven permiten reproducir el entorno local.
- [x] No se versionan `.env`, `target/`, `build/`, `logs/`, `.idea/` ni `.refact/`.

## Producto certificado

- [x] Backend compila: `mvn clean verify` y `mvn package` locales exitosos.
- [x] Backend real responde `UP`, OpenAPI y Swagger operativos.
- [x] PostgreSQL 17.11 real y Flyway V1 → V1.1 → V2 certificados.
- [x] Seed y catálogo de demostración disponibles.
- [x] Android compila: `lintDebug assembleDebug` local exitoso.
- [x] APK debug generado y smoke ejecutado en `emulator-5554` contra el backend real.
- [x] Adaptación automática y pista demostradas con persistencia real.
- [x] No existe selector manual de dificultad en la demo.
- [x] Tests del motor verdes: 12 pruebas, 0 fallos.
- [x] CI verde: Backend CI y Android CI exitosos en GitHub Actions.

## Sustentación y documentación

- [x] Matriz maestra del Taller: `06_CUMPLIMIENTO_TALLER_001.md`.
- [x] Diagramas de contexto, pipeline, componentes, secuencia, modelo de datos y despliegue disponibles.
- [x] Guion de presentación de 3 minutos.
- [x] Guion de demo de 5 minutos.
- [x] Preparación para revisión y reto técnico.
- [x] Código relevante identificado en README y documento técnico.
- [ ] Documento técnico verificado visualmente en un formato de exportación de máximo dos páginas.

## Cierre responsable

La única casilla pendiente no corresponde a una funcionalidad: requiere una comprobación visual del PDF o formato de exportación que determine el docente. No se marca como completada sin esa evidencia. Las validaciones de producto, runtime, CI y release constan en `docs/08-evidence/`.
