# Pipeline Adaptativo

## Pipeline académico

```text
CONTEXTO
aciertos + errores + tiempo + rachas + dificultad
        |
        v
PROCESAMIENTO
ventana reciente + precisión + tiempo medio + consistencia
        |
        v
DECISIÓN
BAJO / MEDIO / ALTO + regla aplicable
        |
        v
ADAPTACIÓN
bajar / mantener / subir dificultad
habilitar pista
cambiar tipo
```

## Separación física sugerida
- Contexto: `LearningContextBuilder`.
- Procesamiento: `PerformanceAnalyzer`.
- Decisión: `AdaptationEngine`.
- Adaptación: `AdaptationActionExecutor`.
- UI: consume `AdaptationDecision`; no decide.
