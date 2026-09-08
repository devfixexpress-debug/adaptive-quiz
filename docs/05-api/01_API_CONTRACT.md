# Contrato REST inicial

**Estado M0/M1:** están implementados GET /asignaturas, GET /asignaturas/{idAsignatura}/temas y GET /ejercicios/{idEjercicio}. Los demás recursos descritos aquí son contratos futuros, no endpoints disponibles aún.

Base: `/api/v1`

## Catálogo académico
- `GET /asignaturas`
- `GET /asignaturas/{id}/temas`

**Implementado.** La respuesta de asignaturas contiene id, código, nombre y descripción. La consulta de temas requiere una asignatura activa.

## Ejercicios
- `GET /ejercicios/siguiente?estudianteId=&temaId=`
- `GET /ejercicios/{id}`

**Implementado sólo GET /ejercicios/{id}.** Devuelve enunciado, clasificación parametrizada, opciones públicas y pistas. No expone 'esCorrecta', retroalimentación ni explicación antes del futuro caso de uso de calificación.

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
  "opciones": [{"id": 1, "codigo": "A", "texto": "2", "orden": 1}],
  "pistas": [{"id": 1, "orden": 1, "texto": "Aísla la variable.", "penalizacionPuntaje": 0.10}]
}
~~~

## Operación técnica implementada

- Health: GET /actuator/health
- OpenAPI JSON: GET /api-docs
- Swagger UI local: GET /swagger-ui/index.html

## Práctica
- `POST /sesiones-practica`
- `POST /intentos`
- `GET /sesiones-practica/{id}`

## Progreso
- `GET /estudiantes/{id}/progreso`
- `GET /estudiantes/{id}/progreso/{temaId}`

## Adaptación
- `GET /estudiantes/{id}/adaptaciones`
- `GET /adaptaciones/{id}`

## Ejemplo futuro: POST /intentos
```json
{
  "sesionPracticaId": 1,
  "ejercicioId": 12,
  "opcionesSeleccionadas": [48],
  "tiempoRespuestaMs": 12400
}
```

## Respuesta futura
```json
{
  "correcto": true,
  "resultado": "CORRECTO",
  "adaptacion": {
    "rendimiento": "ALTO",
    "accion": "SUBIR_DIFICULTAD",
    "dificultadAnterior": "INTERMEDIO",
    "dificultadNueva": "AVANZADO",
    "motivo": "Precisión y tiempo cumplen la política vigente"
  }
}
```
