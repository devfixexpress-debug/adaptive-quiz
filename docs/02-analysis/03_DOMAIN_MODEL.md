# Modelo de Dominio

## Agregados/conceptos principales

### Contenido académico
- Asignatura
- Tema
- Ejercicio
- Opción
- Pista

### Aprendizaje
- Estudiante
- ProgresoTema
- SesionPractica
- Intento
- Respuesta

### Adaptación
- PoliticaAdaptacion
- ReglaAdaptacion
- ContextoAprendizaje
- EventoAdaptacion
- AccionAdaptacion

## Relaciones esenciales
- Una asignatura tiene muchos temas.
- Un tema puede tener subtemas.
- Un tema contiene ejercicios.
- Un ejercicio tiene opciones y pistas.
- Un estudiante inicia sesiones de práctica.
- Una sesión contiene intentos.
- Cada intento corresponde a un ejercicio.
- Un intento puede generar un contexto.
- Un contexto puede generar un evento de adaptación.
- Un evento puede ejecutar varias acciones.

## Regla ontológica
`Ejercicio` es definición/catálogo de contenido; `Intento` es un hecho real. No deben
mezclarse atributos de la definición del ejercicio con atributos del evento de resolución.
