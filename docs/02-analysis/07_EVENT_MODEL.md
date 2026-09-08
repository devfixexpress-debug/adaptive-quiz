# Modelo de Eventos

## Cadena principal

```text
SESION_INICIADA
      |
      v
EJERCICIO_PRESENTADO
      |
      v
RESPUESTA_REGISTRADA
      |
      v
INTENTO_RESUELTO
      |
      +--> actualiza APR_PROGRESO_TEMA
      |
      v
CONTEXTO_CALCULADO
      |
      v
DECISION_ADAPTATIVA
      |
      v
ACCION_APLICADA
```

## Evento crítico
`INTENTO_RESUELTO` es el disparador del pipeline adaptativo.

## Persistencia
- Evento operativo: `PRA_INTENTO`.
- Snapshot contextual: `ADP_CONTEXTO_APRENDIZAJE`.
- Decisión: `ADP_EVENTO_ADAPTACION`.
- Acciones: `ADP_ACCION_EVENTO`.

Esta separación permite reconstruir por qué el sistema tomó una decisión.
