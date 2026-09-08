# Reglas de Decisión

## Evaluación
1. Cargar política activa.
2. Cargar reglas activas ordenadas por `prioridad ASC`.
3. Evaluar condiciones no nulas de cada regla.
4. Aplicar la primera regla que coincida.
5. Generar evento y acciones.
6. Aplicar límites de dificultad.

## Política inicial
| Prioridad | Regla | Condición | Resultado | Acción |
|---:|---|---|---|---|
| 10 | R_BAJO_RACHA | racha errores >= 3 | BAJO | BAJAR_DIFICULTAD + PISTA |
| 20 | R_BAJO_PRECISION | precisión <= 0.40 | BAJO | BAJAR_DIFICULTAD + PISTA |
| 30 | R_ALTO | precisión >= 0.80 y tiempo <= 20000 ms | ALTO | SUBIR_DIFICULTAD |
| 90 | R_MEDIO | fallback | MEDIO | MANTENER_DIFICULTAD |

## Límites
BASICO <- INTERMEDIO -> AVANZADO, sin salir del rango.
