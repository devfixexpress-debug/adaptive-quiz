# ADR-0002 — Backend modular con generador VAEF

**Estado:** Aceptado

## Contexto
AdaptiveQuiz no pertenece a VAEF, pero existe un generador backend maduro dentro del
workspace.

## Decisión
Reutilizar el generador como herramienta, manteniendo namespace y documentación propios del
proyecto.

## Consecuencia
No se debe crear dependencia funcional hacia servicios VAEF.
