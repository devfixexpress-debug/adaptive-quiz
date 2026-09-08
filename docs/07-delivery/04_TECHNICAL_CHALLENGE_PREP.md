# Preparación para reto técnico

## Cambios probables
- cambiar umbral 80% -> 70%;
- cambiar ventana 5 -> 3;
- agregar condición por racha;
- evitar subida cuando ya está avanzado;
- habilitar pista después de dos errores;
- incorporar otro tipo de ejercicio;
- explicar por qué una regla ganó.

## Diseño para responder rápido
- umbrales en `CFG_PARAMETRO`/`CFG_REGLA_ADAPTACION`;
- reglas ordenadas por prioridad;
- motor sin dependencias UI;
- pruebas por cada regla;
- historial auditable.
