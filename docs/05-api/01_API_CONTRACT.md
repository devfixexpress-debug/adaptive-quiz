# Contrato REST

Base del API: `/api/v1`
Formato: JSON UTF-8. Las rutas y parámetros de identificador aceptan únicamente valores positivos.

Los errores se devuelven con estado HTTP y el contrato uniforme
`timestamp`, `status`, `error`, `codigo`, `message` y `path`.

## Endpoints disponibles

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/asignaturas` | Lista asignaturas académicas activas. |
| GET | `/asignaturas/{id}/temas` | Lista temas activos de una asignatura. |
| GET | `/ejercicios/{id}` | Obtiene el contenido visible de un ejercicio. |
| GET | `/ejercicios/siguiente?estudianteId={id}&temaId={id}` | Selecciona la siguiente experiencia según el progreso adaptativo. |
| POST | `/sesiones-practica` | Crea una sesión de práctica y resuelve/crea el progreso del tema. |
| POST | `/intentos` | Califica un intento, actualiza el progreso y ejecuta la adaptación. |
| GET | `/estudiantes/{id}/progreso` | Lista el progreso del estudiante por tema. |
| GET | `/estudiantes/{id}/progreso/{temaId}` | Obtiene el progreso del estudiante en un tema. |
| GET | `/estudiantes/{id}/adaptaciones` | Lista las decisiones adaptativas persistidas del estudiante. |
| GET | `/adaptaciones/{id}` | Obtiene contexto, decisión y acciones de una adaptación. |
| GET | `/configuracion-adaptativa` | Consulta la política activa y los umbrales docentes permitidos. |
| PATCH | `/configuracion-adaptativa/reglas/{codigoRegla}` | Modifica sólo umbrales permitidos de `R_ALTO`, `R_BAJO_PRECISION` o `R_BAJO_RACHA`. |
| PATCH | `/configuracion-adaptativa/politica` | Modifica la ventana de intentos de la política activa. |
| POST | `/configuracion-adaptativa/restaurar-taller` | Restaura los valores certificados del Taller sin tocar datos de aprendizaje. |

## Catálogo académico

### GET /asignaturas

Respuesta `200 OK`:

~~~json
[
  {
    "id": 1,
    "codigo": "MAT",
    "nombre": "Matemática",
    "descripcion": "Asignatura de demostración"
  }
]
~~~

### GET /asignaturas/{id}/temas

Respuesta `200 OK`:

~~~json
[
  {
    "id": 1,
    "idAsignatura": 1,
    "codigo": "ALG",
    "nombre": "Álgebra",
    "descripcion": "Ecuaciones y operaciones algebraicas",
    "orden": 10
  }
]
~~~

## Ejercicios

### GET /ejercicios/{id}

Devuelve únicamente el contenido visible; las opciones no exponen su clave ni
retroalimentación antes de la calificación.

Respuesta `200 OK`:

~~~json
{
  "id": 1,
  "idTema": 1,
  "codigo": "ALG-B-001",
  "enunciado": "2x + 4 = 12. ¿Cuánto vale x?",
  "tipoEjercicio": "OPCION_UNICA",
  "dificultad": "BASICO",
  "estado": "PUBLICADO",
  "tiempoObjetivoSegundos": 20,
  "puntajeBase": 1.00,
  "opciones": [
    {"id": 1, "codigo": "A", "texto": "2", "orden": 1}
  ],
  "pistas": [
    {"id": 1, "orden": 1, "texto": "Aísla primero la variable.", "penalizacionPuntaje": 0.10}
  ]
}
~~~

### GET /ejercicios/siguiente

Parámetros requeridos: `estudianteId`, `temaId`.

La selección usa la dificultad del progreso, ejercicios publicados y un orden estable con
exclusión razonable de ejercicios ya usados en la sesión. Si se agota el nivel, la respuesta
indica `fallbackPorAgotamiento`.

Respuesta `200 OK`: objeto con `ejercicio`, `progreso`, `pistaHabilitada`,
`numeroPregunta` y `fallbackPorAgotamiento`.

## Sesión de práctica

### POST /sesiones-practica

Solicitud:

~~~json
{
  "estudianteId": 1,
  "temaId": 1
}
~~~

Respuesta `201 Created`: `idSesionPractica`, `estudianteId`, `temaId`,
`politicaAdaptacionId`, `estado`, `fechaInicio`, `cantidadIntentos` y `progreso`.
Si no hay progreso previo, el servicio crea uno con la dificultad inicial configurada por la
política.

## Registro de intento y adaptación

### POST /intentos

Solicitud:

~~~json
{
  "sesionPracticaId": 1,
  "ejercicioId": 12,
  "opcionesSeleccionadas": [48],
  "tiempoRespuestaMs": 12400,
  "usoPista": false
}
~~~

`usoPista` es opcional y se interpreta como `false` cuando no se envía.

Respuesta `201 Created`:

~~~json
{
  "correcto": true,
  "resultado": "CORRECTO",
  "explicacion": "Restar 4 y dividir entre 2.",
  "progreso": {
    "idProgresoTema": 1,
    "estudianteId": 1,
    "temaId": 1,
    "dificultadActual": "AVANZADO",
    "totalIntentos": 5,
    "totalAciertos": 4,
    "porcentajeAcierto": 0.80,
    "tiempoPromedioMs": 14200,
    "rachaAciertosActual": 3,
    "rachaErroresActual": 0,
    "fechaUltimoIntento": "2026-09-08T10:15:30-05:00"
  },
  "adaptacion": {
    "idAdaptacion": 1,
    "rendimiento": "ALTO",
    "regla": "R_ALTO",
    "accionPrincipal": "SUBIR_DIFICULTAD",
    "dificultadAnterior": "INTERMEDIO",
    "dificultadNueva": "AVANZADO",
    "tipoEjercicioAnterior": "OPCION_UNICA",
    "tipoEjercicioNuevo": "OPCION_UNICA",
    "pistaHabilitada": false,
    "motivo": "Precisión y tiempo cumplen la política vigente.",
    "versionMotor": "1.0.0",
    "fechaDecision": "2026-09-08T10:15:30-05:00",
    "aplicacionExitosa": true,
    "contexto": {
      "idContextoAprendizaje": 1,
      "intentoDisparadorId": 5,
      "estudianteId": 1,
      "temaId": 1,
      "numeroIntentosVentana": 5,
      "totalAciertosVentana": 4,
      "porcentajeAcierto": 0.80,
      "tiempoPromedioMs": 14200,
      "rachaAciertos": 3,
      "rachaErrores": 0,
      "dificultadActual": "INTERMEDIO",
      "tipoEjercicioActual": "OPCION_UNICA",
      "puntajeRendimiento": 0.94
    },
    "acciones": [
      {
        "secuencia": 1,
        "accion": "SUBIR_DIFICULTAD",
        "detalle": "Ajuste automático de dificultad",
        "valorAnterior": "INTERMEDIO",
        "valorNuevo": "AVANZADO",
        "ejecutada": true
      }
    ]
  }
}
~~~

La adaptación representa la ejecución real del pipeline: contexto, procesamiento, decisión y
acciones persistidas.

## Progreso

### GET /estudiantes/{id}/progreso

Respuesta `200 OK`: arreglo de objetos de progreso. Cada objeto incluye `idProgresoTema`,
`estudianteId`, `temaId`, `dificultadActual`, `totalIntentos`, `totalAciertos`,
`porcentajeAcierto`, `tiempoPromedioMs`, `rachaAciertosActual`,
`rachaErroresActual` y `fechaUltimoIntento`.

### GET /estudiantes/{id}/progreso/{temaId}

Respuesta `200 OK`: un objeto con el mismo contrato de progreso. Devuelve `404 Not Found`
si aún no existe progreso para esa combinación de estudiante y tema.

## Historial y detalle de adaptación

### GET /estudiantes/{id}/adaptaciones

Respuesta `200 OK`: arreglo de decisiones adaptativas. Cada decisión incluye rendimiento,
regla aplicada, acción principal, dificultad anterior/nueva, indicador de pista, motivo, versión
del motor, contexto y acciones ejecutadas.

### GET /adaptaciones/{id}

Respuesta `200 OK`: una decisión adaptativa con el mismo contrato detallado. Devuelve
`404 Not Found` si no existe.

## Configuración adaptativa para uso docente

Estos endpoints administran política, reglas y umbrales persistidos. No permiten seleccionar ni
asignar manualmente la dificultad de un estudiante, ni cambiar códigos, prioridad, acción,
catálogos o datos de práctica.

### GET /configuracion-adaptativa

Respuesta `200 OK`:

~~~json
{
  "politica": {
    "codigo": "POLITICA_BASE_TALLER",
    "nombre": "Política base Taller 001",
    "version": 1,
    "tamanoVentanaIntentos": 5
  },
  "reglas": [
    {
      "codigo": "R_ALTO",
      "nombre": "Alto rendimiento",
      "prioridad": 30,
      "porcentajeAciertoMin": 0.80,
      "porcentajeAciertoMax": null,
      "tiempoPromedioMaxMs": 20000,
      "rachaErroresMin": null,
      "habilitarPista": false
    }
  ]
}
~~~

### PATCH /configuracion-adaptativa/reglas/{codigoRegla}

Sólo acepta campos habilitados para la regla indicada. El código debe ser `R_ALTO`,
`R_BAJO_PRECISION` o `R_BAJO_RACHA`.

Ejemplo para subir con 70 %:

~~~json
{
  "porcentajeAciertoMin": 0.70
}
~~~

Ejemplo para reforzar con dos errores consecutivos:

~~~json
{
  "rachaErroresMin": 2,
  "habilitarPista": true
}
~~~

La respuesta `200 OK` devuelve la configuración completa vigente. Los porcentajes deben estar
entre `0` y `1`, el tiempo y rachas deben ser positivos o cero, y las restricciones del modelo se
validan antes de persistir.

### PATCH /configuracion-adaptativa/politica

Solicitud para cambiar la ventana reciente:

~~~json
{
  "tamanoVentanaIntentos": 3
}
~~~

El valor permitido es de `1` a `50`, de acuerdo con el `CHECK` certificado de
`CFG_POLITICA_ADAPTACION`. La respuesta `200 OK` devuelve la configuración actualizada.

### POST /configuracion-adaptativa/restaurar-taller

No requiere cuerpo. Restaura exclusivamente los valores base de `POLITICA_BASE_TALLER`:

- ventana: `5`;
- `R_BAJO_RACHA.racha_errores_min`: `3`;
- `R_BAJO_PRECISION.porcentaje_acierto_max`: `0.40`;
- `R_ALTO.porcentaje_acierto_min`: `0.80`;
- `R_ALTO.tiempo_promedio_max_ms`: `20000`.

No modifica intentos, contextos, eventos, progreso, ejercicios ni estudiantes.
