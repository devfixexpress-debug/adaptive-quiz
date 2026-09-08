# Contrato REST inicial

Base: `/api/v1`

## Catálogo académico
- `GET /asignaturas`
- `GET /asignaturas/{id}/temas`

## Ejercicios
- `GET /ejercicios/siguiente?estudianteId=&temaId=`
- `GET /ejercicios/{id}`

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

## Ejemplo POST /intentos
```json
{
  "sesionPracticaId": 1,
  "ejercicioId": 12,
  "opcionesSeleccionadas": [48],
  "tiempoRespuestaMs": 12400
}
```

## Respuesta
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
