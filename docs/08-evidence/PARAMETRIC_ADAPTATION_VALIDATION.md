# Validación de adaptación paramétrica

**Fecha:** 2026-09-08

**Entorno:** PostgreSQL 17.11 en `localhost:55432`, Spring Boot 1.0.0 en `localhost:8080` y Android en `emulator-5554`.

**Alcance:** cambio controlado de parámetros existentes. No se modificaron migraciones, tablas, catálogos, intentos previos ni el algoritmo adaptativo.

## Configuración inicial observada

`GET /api/v1/configuracion-adaptativa` respondió la política activa `POLITICA_BASE_TALLER`, versión `1`:

| Valor persistido | Inicial |
|---|---:|
| `tamano_ventana_intentos` | 5 |
| `R_ALTO.porcentaje_acierto_min` | 0.8000 |
| `R_ALTO.tiempo_promedio_max_ms` | 20000 |
| `R_BAJO_PRECISION.porcentaje_acierto_max` | 0.4000 |
| `R_BAJO_RACHA.racha_errores_min` | 3 |
| Pista en reglas de refuerzo | activa |

El mismo proceso Spring Boot (PID `22408`) atendó las consultas, modificaciones e intentos siguientes. No se ejecutó recompilación ni reinicio entre el cambio y la decisión.

## Cambio en vivo: 80 % a 70 %

Comandos REST ejecutados contra la instancia activa:

```text
PATCH /api/v1/configuracion-adaptativa/reglas/R_ALTO
{"porcentajeAciertoMin": 0.70}

PATCH /api/v1/configuracion-adaptativa/politica
{"tamanoVentanaIntentos": 3}
```

La consulta posterior devolvió `R_ALTO.porcentajeAciertoMin = 0.7000` y ventana `3`. Para demostrar una precisión exacta de 75 %, la ventana se ajustó temporalmente a `4`; después se inició la sesión real `5` y se registraron cuatro intentos de Álgebra con tiempos de `10000 ms`: correcto, correcto, incorrecto, correcto.

La cuarta decisión persistida produjo:

| Dato observado | Valor |
|---|---|
| Intento / evento | `18` / `18` |
| Ventana | 4 intentos |
| Aciertos de ventana | 3 |
| Precisión | 0.7500 |
| Tiempo promedio | 10000 ms |
| Regla aplicada | `R_ALTO` |
| Rendimiento / acción | `ALTO` / `SUBIR_DIFICULTAD` |

El 75 % no alcanza el umbral inicial de 80 %, pero sí el umbral persistido de 70 %. La evidencia demuestra que la siguiente evaluación leyó la configuración nueva sin recompilar ni reiniciar.

## Cambio en vivo: racha de 3 a 2 y pista

Se ejecutó:

```text
PATCH /api/v1/configuracion-adaptativa/reglas/R_BAJO_RACHA
{"rachaErroresMin": 2, "habilitarPista": true}
```

Dos intentos incorrectos consecutivos en la sesión `5` generaron el evento `20`:

| Dato observado | Valor |
|---|---|
| Racha de errores observada | 2 |
| Precisión de ventana | 0.2500 |
| Regla / rendimiento | `R_BAJO_RACHA` / `BAJO` |
| Acción principal | `BAJAR_DIFICULTAD` |
| Acción adicional | `ACTIVAR_PISTA` ejecutada |

La verificación directa de PostgreSQL encontró las acciones `BAJAR_DIFICULTAD` y `ACTIVAR_PISTA` en `ADP_ACCION_EVENTO` para el evento `20`. Al finalizar quedaron `20` filas en cada una de `PRA_INTENTO`, `ADP_CONTEXTO_APRENDIZAJE` y `ADP_EVENTO_ADAPTACION`, y `25` en `ADP_ACCION_EVENTO`; los datos preexistentes no fueron reiniciados.

## Restauración del Taller

Se ejecutó sin cuerpo:

```text
POST /api/v1/configuracion-adaptativa/restaurar-taller
```

La consulta REST posterior y una consulta directa a PostgreSQL confirmaron el baseline final:

| Valor restaurado | Final |
|---|---:|
| Ventana | 5 |
| `R_ALTO` precisión mínima | 0.8000 |
| `R_ALTO` tiempo máximo | 20000 ms |
| `R_BAJO_PRECISION` precisión máxima | 0.4000 |
| `R_BAJO_RACHA` racha mínima | 3 |
| Pista de `R_BAJO_RACHA` | `true` |

El endpoint de restauración no modifica `PRA_*`, `APR_*`, `ADP_*`, ejercicios ni estudiantes; sólo repone los valores centralizados en el baseline de configuración.

## Validaciones de mantenimiento limitado

Dos solicitudes inválidas se rechazaron sin modificar la configuración:

| Solicitud | Resultado real |
|---|---|
| `PATCH /configuracion-adaptativa/reglas/R_MEDIO` | HTTP `400`, mensaje: sólo se pueden modificar `R_ALTO`, `R_BAJO_PRECISION` o `R_BAJO_RACHA`. |
| `PATCH /configuracion-adaptativa/politica` con `{"tamanoVentanaIntentos": 0}` | HTTP `400`, mensaje: el tamaño de ventana debe ser al menos 1. |

Los errores se entregaron con el contrato uniforme de validación en español.

## Comprobaciones complementarias

- `GET /actuator/health`: `UP` después de la restauración.
- `GET /api-docs`: HTTP `200`; el contrato expone GET, PATCH de reglas, PATCH de política y POST de restauración.
- El APK debug actualizado se instaló en `emulator-5554`, abrió sin fallo fatal y cargó la pantalla **Configuración adaptativa** con los valores restaurados: ventana `5`, precisión alta `80 %`, tiempo `20 segundos`, precisión de refuerzo `40 %` y racha `3`.

La configuración administra políticas y umbrales; no permite elegir manualmente la dificultad del estudiante.
