# Estrategia de Pruebas

## Prioridad 1 — Motor adaptativo
Pruebas unitarias puras, sin Spring:
- cálculo de precisión;
- rachas;
- regla alta;
- regla baja por precisión;
- regla baja por racha;
- fallback medio;
- límites superior/inferior.

## Prioridad 2 — Dominio
- ejercicio de opción única tiene una correcta;
- tiempo no negativo;
- sesión e intento consistentes.

## Prioridad 3 — Backend
- controllers;
- services;
- repositories;
- migración limpia PostgreSQL.

## Prioridad 4 — Integración
Mobile -> API -> PostgreSQL.

## Criterio de certificación
No considerar funcional una historia crítica sólo por mock si existe posibilidad de demostrarla
con runtime real.
