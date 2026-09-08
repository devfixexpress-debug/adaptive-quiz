# Prompt de implementación para Codex

Trabaja exclusivamente dentro de `adaptive-quiz/`.

## Objetivo
Implementar Taller 001 de SI806 siguiendo la documentación existente. No rediseñes el dominio
sin registrar un ADR.

## Reglas obligatorias
1. AdaptiveQuiz es independiente de VAEF; sólo puede reutilizar el generador/estándares.
2. Backend modular:
   `domain`, `application`, `infrastructure`, `api`.
3. DB: PostgreSQL; nombres de tablas y columnas en español; respetar DDL de `database/`.
4. No hardcodear estados, dificultades, tipos ni acciones que ya existan en `CAT_*`.
5. El motor base es determinista; IA queda detrás de `AdaptationStrategy`.
6. Separar:
   `context -> processing -> decision -> adaptation`.
7. Toda regla crítica debe tener prueba.
8. Mantener `TRACEABILITY_MATRIX.md`.
9. Registrar cada cambio relevante en `CHANGELOG.md`.
10. No introducir mocks como evidencia final si puede probarse runtime real.

## Orden de trabajo
A. Validar documentación y DDL.
B. Ejecutar migración PostgreSQL limpia.
C. Generar backend usando el generador VAEF como herramienta.
D. Implementar endpoints.
E. Crear Android Kotlin/Compose.
F. Implementar quiz.
G. Implementar motor adaptativo.
H. Integrar.
I. Añadir pruebas/CI.
J. Preparar release.

## Gate M4
No avances a IA hasta que:
- adaptación por reglas funcione;
- tests estén verdes;
- evento adaptativo se persista;
- demo sea reproducible.
