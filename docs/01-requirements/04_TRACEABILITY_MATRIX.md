# Matriz de Trazabilidad

**Corte M0/M1:** sólo se actualizan requisitos con implementación o scaffolding verificable. La evidencia de runtime real está en 'docs/08-evidence/BACKEND_RUNTIME_VALIDATION.md'.

| Requisito | HU | Componente | Tabla principal | Issue | Test / evidencia | Estado |
|---|---|---|---|---|---|---|
| RF-001 | - | 'CatalogoAcademicoQueryService', 'CatalogoAcademicoController' | ACA_ASIGNATURA, ACA_TEMA | - | EVD-RUNTIME-001: GET asignaturas y temas | Implementado |
| RF-002 | HU-001 | QuizService | BAN_EJERCICIO | TBD | TC-001 | Pendiente |
| RF-003 | HU-001 | 'EjercicioQueryService', 'EjercicioController' | BAN_EJERCICIO, BAN_OPCION_EJERCICIO, BAN_PISTA_EJERCICIO | - | EVD-RUNTIME-001: GET ejercicio 1 | Implementado |
| RF-006 | HU-002 | AttemptService | PRA_INTENTO | TBD | TC-002 | Pendiente |
| RF-009 | - | 'EjercicioRepository' y adaptador JPA | BAN_EJERCICIO | - | EVD-DB-001 + EVD-RUNTIME-001 | Parcial: recuperación implementada; administración/persistencia por caso de uso pendiente |
| RF-010 | - | API REST, OpenAPI y Actuator | - | - | EVD-RUNTIME-001 | Parcial: sólo el primer vertical slice |
| RA-003 | HU-003 | LearningContextBuilder | ADP_CONTEXTO_APRENDIZAJE | TBD | TC-010 | Pendiente |
| RA-004 | HU-003 | PerformanceAnalyzer | ADP_CONTEXTO_APRENDIZAJE | TBD | TC-011 | Pendiente |
| RA-005 | HU-003 | AdaptationEngine | ADP_EVENTO_ADAPTACION | TBD | TC-012 | Pendiente |
| RA-007 | HU-003 | AdaptationEngine | ADP_EVENTO_ADAPTACION | TBD | TC-013 | Pendiente |
| RA-008 | HU-004 | AdaptationActionExecutor | ADP_ACCION_EVENTO | TBD | TC-014 | Pendiente |
| RA-010 | HU-005 | LearningContextBuilder | ADP_CONTEXTO_APRENDIZAJE | TBD | TC-015 | Pendiente |
| RA-011 | HU-005 | AdaptationAudit | ADP_EVENTO_ADAPTACION | TBD | TC-016 | Pendiente |
| RA-012 | HU-005 | AdaptiveMonitor | - | TBD | TC-017 | Pendiente |
| RNF-001 | HU-007 | módulos domain/application/infrastructure/API y contratos de adaptación | - | - | 'mvn clean verify'; EVD-RUNTIME-001 | Parcial: separación y contratos creados; motor aún no implementado |
| RNF-006 | - | Flyway, DDL y PostgreSQL | todas las tablas del modelo | - | EVD-DB-001 | Implementado y validado |
| RNF-007 | - | Spring Boot, PostgreSQL y 'AdaptationStrategy' | - | - | EVD-RUNTIME-001 | Implementado para foundation; no hay IA |
| RFAI-001 | - | 'AdaptationStrategy' | futuro | - | revisión de contrato | Preparado: contrato extensible; estrategia IA no implementada |
