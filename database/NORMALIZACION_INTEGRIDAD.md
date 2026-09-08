# Normalización e Integridad

## 3FN
El modelo se mantiene en tercera forma normal para las entidades de fuente de verdad:
catálogos, contenido, sesiones, intentos, respuestas y eventos.

## Derivados intencionales

### APR_PROGRESO_TEMA
Contiene contadores/estadísticas para consulta rápida. Es un resumen materializado y puede
recalcularse desde `PRA_INTENTO`.

### ADP_CONTEXTO_APRENDIZAJE
Contiene un snapshot deliberadamente redundante de métricas para explicar una decisión en el
tiempo. No reemplaza la fuente de verdad.

## Integridad
- PK para cada entidad.
- UK para códigos semánticos.
- FK para asociaciones.
- CHECK para rangos.
- Índices en FK de navegación frecuente.
- Restricción de vigencia `hasta >= desde`.
- `BAN_OPCION_EJERCICIO`: la regla de exactamente una correcta por ejercicio de opción única
  se valida en dominio/servicio y prueba de integración; puede endurecerse con trigger si se
  requiere.

## Eliminación
En v1 se prefiere baja lógica (`activo`/estado) en maestros. No usar cascadas destructivas sobre
historial de intentos ni eventos adaptativos.
