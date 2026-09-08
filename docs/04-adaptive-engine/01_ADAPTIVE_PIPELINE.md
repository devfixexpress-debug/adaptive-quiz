# Pipeline adaptativo certificado

## Pipeline académico

```text
CONTEXTO
intentos recientes + aciertos + errores + tiempo + rachas + dificultad
        │
        ▼
PROCESAMIENTO
precisión + tiempo promedio + consistencia + clasificación
        │
        ▼
DECISIÓN
BAJO / MEDIO / ALTO + regla aplicable por prioridad
        │
        ▼
ADAPTACIÓN
subir / mantener / bajar dificultad
activar pista cuando corresponde
        │
        ▼
SIGUIENTE EXPERIENCIA
ejercicio seleccionado según progreso actualizado
```

## Separación física implementada

- **Contexto:** `LearningContextBuilder` obtiene una ventana móvil de hasta cinco intentos recientes disponibles, según `tamano_ventana_intentos`.
- **Procesamiento:** `DefaultPerformanceAnalyzer` implementa `PerformanceAnalyzer`.
- **Decisión:** `AdaptationEngine` delega en `RuleBasedAdaptationStrategy`.
- **Adaptación/persistencia:** `AdaptationActionPersistenceService` implementa `AdaptationActionExecutor`.
- **Orquestación:** `PracticaService` registra el intento y compone el flujo transaccional.
- **UI:** consume `AdaptationDecision` mediante API; no decide ni contiene umbrales.

## Alcance de v1.0.0

La demo certificada adapta dificultad y disponibilidad de pista. El tipo de ejercicio está modelado, pero no se presenta como una adaptación activa de la demo principal. No hay IA conectada.
