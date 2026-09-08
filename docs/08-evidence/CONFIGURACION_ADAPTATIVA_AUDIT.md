# Auditoría de configuración adaptativa

**Fecha:** 2026-09-08
**Ámbito:** valores existentes en PostgreSQL 17.11 y consumidores reales del backend. No se crearon tablas, columnas ni migraciones.

## Resultado de la lectura en runtime

La política activa observada es `POLITICA_BASE_TALLER`, versión `1`, con ventana `5`. Las reglas activas se leen de `CFG_REGLA_ADAPTACION` en orden de prioridad ascendente y no se almacenan en caché dentro del motor. `PracticaService` recupera la política de la sesión en cada registro de intento y crea un motor nuevo mediante `MotorAdaptativoFactory`; el adaptador JDBC consulta PostgreSQL en cada acceso.

| Parámetro | Tabla | Código/regla | Valor actual | Consumidor |
|---|---|---|---|---|
| Tamaño de ventana | `CFG_POLITICA_ADAPTACION` | `POLITICA_BASE_TALLER.tamano_ventana_intentos` | `5` | `PracticaService` entrega la política a `LearningContextBuilder`, que limita la ventana reciente. |
| Racha mínima de errores | `CFG_REGLA_ADAPTACION` | `R_BAJO_RACHA.racha_errores_min` | `3` | `RuleBasedAdaptationStrategy.cumple`; al coincidir genera la decisión BAJO. |
| Pista por racha | `CFG_REGLA_ADAPTACION` | `R_BAJO_RACHA.habilitar_pista` | `true` | `RuleBasedAdaptationStrategy.crearAcciones`; agrega `ACTIVAR_PISTA`. |
| Precisión máxima de refuerzo | `CFG_REGLA_ADAPTACION` | `R_BAJO_PRECISION.porcentaje_acierto_max` | `0.4000` | `RuleBasedAdaptationStrategy.cumple`; compara precisión observada con el máximo. |
| Pista por precisión baja | `CFG_REGLA_ADAPTACION` | `R_BAJO_PRECISION.habilitar_pista` | `true` | `RuleBasedAdaptationStrategy.crearAcciones`; agrega `ACTIVAR_PISTA`. |
| Precisión mínima para subir | `CFG_REGLA_ADAPTACION` | `R_ALTO.porcentaje_acierto_min` | `0.8000` | `RuleBasedAdaptationStrategy.cumple`; compara precisión observada con el mínimo. |
| Tiempo máximo para subir | `CFG_REGLA_ADAPTACION` | `R_ALTO.tiempo_promedio_max_ms` | `20000` ms | `RuleBasedAdaptationStrategy.cumple`; compara el promedio de la ventana. |
| Pista para rendimiento alto | `CFG_REGLA_ADAPTACION` | `R_ALTO.habilitar_pista` | `false` | `RuleBasedAdaptationStrategy.crearAcciones`. Se conserva consultable, aunque la UI docente prioriza las reglas de refuerzo. |
| Fallback medio | `CFG_REGLA_ADAPTACION` | `R_MEDIO` | sin condiciones, prioridad `90` | `RuleBasedAdaptationStrategy`; se aplica cuando no coincide una regla previa. No se expone para edición. |
| Peso de precisión | `CFG_PARAMETRO` | `PESO_PRECISION` | `0.600000` | `MotorAdaptativoFactory` → `DefaultPerformanceAnalyzer`; afecta únicamente el puntaje explicativo. |
| Peso de velocidad | `CFG_PARAMETRO` | `PESO_VELOCIDAD` | `0.250000` | `MotorAdaptativoFactory` → `DefaultPerformanceAnalyzer`; afecta únicamente el puntaje explicativo. |
| Peso de consistencia | `CFG_PARAMETRO` | `PESO_CONSISTENCIA` | `0.150000` | `MotorAdaptativoFactory` → `DefaultPerformanceAnalyzer`; afecta únicamente el puntaje explicativo. |
| Tiempo rápido auxiliar | `CFG_PARAMETRO` | `TIEMPO_RAPIDO_MS` | `20000` ms | `MotorAdaptativoFactory` → `DefaultPerformanceAnalyzer`; no es el umbral de `R_ALTO`. |
| Racha auxiliar histórica | `CFG_PARAMETRO` | `RACHA_ERRORES_REFUERZO` | `3` | **No tiene consumidor runtime actual**; la decisión usa `R_BAJO_RACHA.racha_errores_min`. No se expone para edición. |
| Dificultad inicial | `CFG_PARAMETRO` | `DIFICULTAD_INICIAL_CODIGO` | `INTERMEDIO` | `PracticaService.nuevoProgreso`; no es un selector de estudiante ni parte del reto docente. |
| Tipo inicial | `CFG_PARAMETRO` | `TIPO_EJERCICIO_INICIAL_CODIGO` | `OPCION_UNICA` | `PracticaService.tipoParaSiguienteExperiencia`; no se modifica en esta tarea. |
| Versión del motor | `CFG_PARAMETRO` | `VERSION_MOTOR_REGLAS` | `1.0.0` | `AdaptationActionPersistenceService`; se persiste con cada evento. |

## Conclusión de diseño

Los valores administrables para la sustentación ya están respaldados por el modelo certificado: una columna de ventana en `CFG_POLITICA_ADAPTACION` y columnas de umbral/bandera en `CFG_REGLA_ADAPTACION`. La modificación controlada debe actualizar únicamente esas columnas de la política activa y de las reglas permitidas; nunca asigna manualmente la dificultad del estudiante ni modifica `APR_*`, `PRA_*` o `ADP_*`.
