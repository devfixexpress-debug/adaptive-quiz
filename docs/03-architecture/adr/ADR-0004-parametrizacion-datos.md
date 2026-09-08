# ADR-0004 — Robustez y estabilidad paramétrica

**Estado:** Aceptado

## Decisión
Separar clasificación, configuración, parque real y eventos:
`CAT_*`, `CFG_*`, `ACA/BAN/APR_*`, `PRA/ADP_*`.

Los estados, niveles, tipos y acciones se mantienen como ítems de catálogo. Los umbrales y
políticas son configurables y versionables.
