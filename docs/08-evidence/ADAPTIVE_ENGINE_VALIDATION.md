# Validación del motor adaptativo

Fecha de ejecución: 2026-09-08  
Ámbito: runtime local real; Spring Boot en puerto 8080 y PostgreSQL Docker en puerto host 55432.

## Base validada

- Motor: PostgreSQL 17.11.
- Historial Flyway observado: V1, V1.1 y V2, todas exitosas.
- La migración V2 agrega únicamente los parámetros DIFICULTAD_INICIAL_CODIGO,
  TIPO_EJERCICIO_INICIAL_CODIGO y VERSION_MOTOR_REGLAS.
- V1 y V1.1 no fueron modificadas.
- El backend respondió UP en /actuator/health.

## Pipeline ejecutado

~~~text
PRA_INTENTO
  → LearningContextBuilder
  → DefaultPerformanceAnalyzer
  → RuleBasedAdaptationStrategy
  → AdaptationActionPersistenceService
  → ADP_CONTEXTO_APRENDIZAJE + ADP_EVENTO_ADAPTACION + ADP_ACCION_EVENTO
~~~

LearningContextBuilder tomó la ventana configurable de cinco intentos. El análisis calculó
precisión, tiempo promedio, rachas, dificultad y tipo. La estrategia leyó política/reglas
persistidas, ordenadas por prioridad, y resolvió códigos semánticos de catálogo; no se usan IDs
de catálogo embebidos en Java.

## Evidencia de decisiones persistidas

| Evento | Contexto | Rendimiento / regla | Cambio | Acción |
|---:|---:|---|---|---|
| 1 | 1 | ALTO / R_ALTO | INTERMEDIO → AVANZADO | SUBIR_DIFICULTAD |
| 2 | 2 | MEDIO / R_MEDIO | AVANZADO → AVANZADO | MANTENER_DIFICULTAD |
| 3 | 3 | BAJO / R_BAJO_PRECISION | AVANZADO → INTERMEDIO | BAJAR_DIFICULTAD + ACTIVAR_PISTA |
| 4 | 4 | BAJO / R_BAJO_RACHA | INTERMEDIO → BASICO | BAJAR_DIFICULTAD + ACTIVAR_PISTA |
| 5 | 5 | BAJO / R_BAJO_PRECISION | BASICO → BASICO | BAJAR_DIFICULTAD + ACTIVAR_PISTA |
| 6 | 6 | BAJO / R_BAJO_PRECISION | BASICO → BASICO | BAJAR_DIFICULTAD + ACTIVAR_PISTA |
| 7 | 7 | MEDIO / R_MEDIO | BASICO → BASICO | MANTENER_DIFICULTAD |
| 8 | 8 | ALTO / R_ALTO | BASICO → INTERMEDIO | SUBIR_DIFICULTAD |
| 9 | 9 | ALTO / R_ALTO | INTERMEDIO → AVANZADO | SUBIR_DIFICULTAD |

El evento 1 verificó la subida automática. Los eventos 3 y 4 verificaron las dos condiciones de
bajo desempeño y la persistencia de pista. El evento 7 fue creado desde la UI Android en el
emulador. La sesión interactiva posterior creó los eventos 8 y 9, que volvieron a demostrar dos
subidas consecutivas desde BASICO hasta AVANZADO.

## Request directo verificado

~~~json
{
  "sesionPracticaId": 2,
  "ejercicioId": 1,
  "opcionesSeleccionadas": [3],
  "tiempoRespuestaMs": 11000
}
~~~

La respuesta fue CORRECTO, creó el contexto 6, devolvió R_BAJO_PRECISION y dejó dos acciones:
BAJAR_DIFICULTAD y ACTIVAR_PISTA. El ejercicio posterior de la UI creó el evento 7 con
R_MEDIO y MANTENER_DIFICULTAD.

## Persistencia observada al cierre

~~~text
PRA_INTENTO                14
PRA_RESPUESTA              14
PRA_RESPUESTA_OPCION       14
ADP_CONTEXTO_APRENDIZAJE   14
ADP_EVENTO_ADAPTACION      14
ADP_ACCION_EVENTO          18
~~~

Consulta representativa ejecutada contra PostgreSQL real:

~~~sql
SELECT e.id_evento_adaptacion, nivel.codigo, anterior.codigo, nueva.codigo, accion.codigo
FROM ADP_EVENTO_ADAPTACION e
JOIN CAT_ITEM_CATALOGO nivel ON nivel.id_item_catalogo = e.id_item_nivel_rendimiento
JOIN CAT_ITEM_CATALOGO anterior ON anterior.id_item_catalogo = e.id_item_dificultad_anterior
JOIN CAT_ITEM_CATALOGO nueva ON nueva.id_item_catalogo = e.id_item_dificultad_nueva
JOIN CAT_ITEM_CATALOGO accion ON accion.id_item_catalogo = e.id_item_accion_principal
ORDER BY e.id_evento_adaptacion;
~~~

## Conclusión

La certificación se realizó contra datos y persistencia reales, sin mocks ni carpetas de pruebas
nuevas. No existe IA ni llamada directa del motor a un proveedor externo.
