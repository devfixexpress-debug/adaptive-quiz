# Modelo de Rendimiento

## V1
La política base utiliza reglas explícitas, no un modelo estadístico opaco.

### Alto
- precisión >= 80%;
- tiempo promedio <= 20 segundos;
- no existir una regla de prioridad mayor que fuerce refuerzo.

### Bajo
- tres errores consecutivos; o
- precisión <= 40%.

### Medio
Caso restante con ventana suficiente.

## Score auxiliar
Puede mostrarse un score explicativo:

```text
score = 0.60 * precisión_normalizada
      + 0.25 * velocidad_normalizada
      + 0.15 * consistencia_normalizada
```

El score **no sustituye** las reglas certificadas en v1; sirve para observabilidad y evolución.
Pesos configurables en `CFG_PARAMETRO`.
