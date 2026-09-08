# Modelo de Contexto

## Entrada mínima
- número de intentos en ventana;
- aciertos;
- porcentaje de acierto;
- tiempo promedio;
- racha de aciertos;
- racha de errores;
- dificultad actual;
- tipo de ejercicio actual.

## Ventana
Valor inicial: 5 intentos, configurable.

## Snapshot
Cada evaluación genera `ADP_CONTEXTO_APRENDIZAJE`. Se conserva para explicar la decisión
aunque después cambie el progreso del estudiante.
