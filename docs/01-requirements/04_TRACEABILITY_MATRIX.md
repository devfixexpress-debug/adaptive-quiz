# Matriz de Trazabilidad

**Corte M3/M4/M5/M6 — 2026-09-08.** Los estados se actualizan sólo cuando existe código
implementado y evidencia de ejecución. La certificación de esta iteración es directa contra
Spring Boot y PostgreSQL reales; por instrucción de alcance no se agregaron mocks ni nuevas
carpetas de pruebas.

| Requisito | HU | Componente | Tabla principal | Evidencia runtime | Estado |
|---|---|---|---|---|---|
| RF-001 | - | CatalogoAcademicoQueryService, CatalogoAcademicoController | ACA_ASIGNATURA, ACA_TEMA | E2E: GET asignaturas/temas = 200 | Implementado |
| RF-002 | HU-001 | PracticaService.obtenerSiguienteEjercicio, EjercicioController | BAN_EJERCICIO, APR_PROGRESO_TEMA | E2E: GET siguiente para estudiante 1/tema 1 | Implementado |
| RF-003 | HU-001 | EjercicioQueryService, EjercicioController, pantalla Práctica | BAN_EJERCICIO, BAN_OPCION_EJERCICIO, BAN_PISTA_EJERCICIO | E2E: ejercicio ALG-B-001 con opciones y pista | Implementado |
| RF-004 | HU-001 | PracticaService.registrarIntento, PracticaController | PRA_RESPUESTA, PRA_RESPUESTA_OPCION | E2E: POST intento 6 | Implementado |
| RF-005 | HU-001 | PracticaService.calificar | PRA_INTENTO | E2E: respuesta correcta, resultado CORRECTO | Implementado para OPCION_UNICA |
| RF-006 | HU-002 | PracticaService, PracticaViewModel | PRA_INTENTO | E2E: tiempoRespuestaMs=11000 persistido | Implementado |
| RF-007 | HU-006 | ConsultarProgresoUseCase, EstudianteController | APR_PROGRESO_TEMA | E2E: GET progreso de estudiante 1 | Implementado |
| RF-008 | HU-005 | ConsultarAdaptacionUseCase, AdaptacionController | ADP_EVENTO_ADAPTACION, ADP_ACCION_EVENTO | E2E: historial y detalle de adaptación 5 | Implementado |
| RF-009 | - | EjercicioRepositoryJpaAdapter | BAN_EJERCICIO, BAN_OPCION_EJERCICIO, BAN_PISTA_EJERCICIO | PostgreSQL real y GET ejercicio/siguiente | Implementado para banco seed y recuperación |
| RF-010 | - | API REST, OpenAPI y Actuator | - | E2E: OpenAPI/Swagger = 200 | Implementado para el MVP |
| RA-001 | HU-002 | PracticaService.calificar | PRA_INTENTO | E2E: intentos correctos e incorrectos persistidos | Implementado |
| RA-002 | HU-002 | TiempoRespuesta, PracticaViewModel | PRA_INTENTO | E2E Android: tiempo automático enviado y persistido | Implementado |
| RA-003 | HU-003 | LearningContextBuilder | ADP_CONTEXTO_APRENDIZAJE | E2E: contexto 14, ventana 5 | Implementado |
| RA-004 | HU-003 | DefaultPerformanceAnalyzer | ADP_CONTEXTO_APRENDIZAJE | E2E: rendimiento BAJO/ALTO/MEDIO persistido | Implementado |
| RA-005 | HU-003 | RuleBasedAdaptationStrategy, ejecutor | ADP_EVENTO_ADAPTACION | Evento 1: INTERMEDIO → AVANZADO | Implementado |
| RA-006 | HU-003 | RuleBasedAdaptationStrategy, ejecutor | ADP_EVENTO_ADAPTACION | Evento 2: AVANZADO → AVANZADO | Implementado |
| RA-007 | HU-003 | RuleBasedAdaptationStrategy, ejecutor | ADP_EVENTO_ADAPTACION | Eventos 3 y 4: disminución real | Implementado |
| RA-008 | HU-004 | AdaptationActionPersistenceService | ADP_ACCION_EVENTO | Eventos 3–6 con ACTIVAR_PISTA | Implementado |
| RA-009 | - | Modelo y contrato de tipo de ejercicio | CFG_REGLA_ADAPTACION, BAN_EJERCICIO | Sin regla de cambio de tipo en la demo | Modelado; no usado en MVP |
| RA-010 | HU-005 | LearningContextBuilder, AdaptacionJdbcAdapter | ADP_CONTEXTO_APRENDIZAJE | Contextos 1–14 en PostgreSQL | Implementado |
| RA-011 | HU-005 | AdaptationActionPersistenceService | ADP_EVENTO_ADAPTACION, ADP_ACCION_EVENTO | Eventos 1–14 y 18 acciones en PostgreSQL | Implementado |
| RA-012 | HU-005 | MonitorScreen, MonitorViewModel | - | E2E Android: Monitor mostró contexto, decisión y adaptación | Implementado |
| RNF-001 | HU-007 | módulos domain/application/infrastructure/API | - | Código y E2E del pipeline | Implementado |
| RNF-002 | HU-007 | contratos de dominio sin UI ni red | - | Arquitectura separada; no certificada con suite por alcance | Parcial |
| RNF-003 | - | CFG política/reglas/parámetros, migración V2 | CFG_POLITICA_ADAPTACION, CFG_REGLA_ADAPTACION, CFG_PARAMETRO | Flyway V2 y decisiones parametrizadas | Implementado |
| RNF-004 | HU-005 | motivo, regla, contexto y acciones | ADP_EVENTO_ADAPTACION | GET adaptación 5 devuelve motivo/evidencia | Implementado |
| RNF-005 | - | - | - | No se realizó medición formal de latencia | Pendiente |
| RNF-006 | - | Flyway, DDL y PostgreSQL | Todas | V1, V1.1 y V2 aplicadas en PostgreSQL 17.11 | Implementado |
| RNF-007 | - | Spring Boot, PostgreSQL y AdaptationStrategy | - | Backend real UP; sin IA | Implementado |
| RNF-008 | HU-005 | Persistencia adaptativa | ADP_* | 14 eventos/18 acciones verificados | Implementado |
| RNF-009 | - | Configuración por entorno | - | .env.example; sin secreto real versionado | Implementado para demo |
| RNF-010 | - | Compose Material 3 en español | - | E2E Android en emulator-5554 | Implementado |
| RFAI-001 | - | AdaptationStrategy | Futuro | Contrato preparado; no hay integración IA | Preparado |
| RFAI-002 | - | RuleBasedAdaptationStrategy | - | Flujo E2E sin IA | Implementado |
