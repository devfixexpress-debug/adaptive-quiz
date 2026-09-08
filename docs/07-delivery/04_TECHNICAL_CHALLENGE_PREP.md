# Preparación para revisión y reto técnico

## Cómo responder durante la sustentación

| Pregunta probable | Respuesta verificable |
|---|---|
| ¿Cuál es el contexto? | Una ventana móvil de hasta cinco intentos recientes disponibles, según `tamano_ventana_intentos`, con aciertos, precisión, tiempo promedio, rachas, dificultad y tipo. |
| ¿Dónde se procesa? | `LearningContextBuilder` construye el contexto y `DefaultPerformanceAnalyzer` lo convierte en métricas y rendimiento; ninguno pertenece a Compose ni a un controller. |
| ¿Cómo se decide? | `RuleBasedAdaptationStrategy` lee política y reglas parametrizadas, las evalúa por prioridad y selecciona la primera aplicable. |
| ¿Qué cambia realmente? | `AdaptationActionPersistenceService` actualiza progreso y persiste acciones de subir, mantener, bajar dificultad o activar pista. |
| ¿Por qué no hay selector manual? | El propósito del Taller es demostrar adaptación automática a contexto real. La dificultad viene de `APR_PROGRESO_TEMA` y de la política. |
| ¿Cómo se evita bajar de BASICO o subir de AVANZADO? | La estrategia aplica límites de dificultad después de decidir; los eventos 5 y 6 certifican el límite básico. |
| ¿Dónde queda la explicación? | En `ADP_CONTEXTO_APRENDIZAJE`, `ADP_EVENTO_ADAPTACION` y `ADP_ACCION_EVENTO`; el API y Monitor la exponen. |
| ¿La UI toma decisiones? | No. La UI usa ViewModel, repositorio y API; sólo transforma la `AdaptationDecision` recibida en estado visual. |
| ¿Por qué no usar IA? | El alcance certificado es determinista, explicable y suficiente para el Taller. `AdaptationStrategy` deja una extensión futura sin SDK, claves ni conexión externa. |
| ¿Cómo se reproduce? | `.env.example`, Docker Compose, Flyway, Maven, Gradle Wrapper, README, CI y evidencias describen el procedimiento. |

## Cambios que se pueden explicar sin afirmar que ya están implementados

Si el docente plantea una variación, describa la ubicación responsable, no un cambio improvisado:

- **Cambiar umbral o ventana:** configuración de política/reglas o parámetros, según el alcance acordado; no se modifica la UI.
- **Cambiar prioridad de una regla:** `CFG_REGLA_ADAPTACION`, con auditoría posterior de la decisión.
- **Agregar una estrategia futura:** implementar `AdaptationStrategy` sin acoplar `AdaptationEngine` a un proveedor.
- **Agregar otro tipo de ejercicio:** extender la experiencia y la política, preservando el vertical slice de `OPCION_UNICA` certificado.

Ninguno de esos cambios forma parte de la release v1.0.0 ni debe ejecutarse durante la entrega.

## Rutas para una revisión técnica rápida

- Contexto: `backend/adaptive-quiz-application/.../LearningContextBuilder.java`.
- Procesamiento: `backend/adaptive-quiz-domain/.../DefaultPerformanceAnalyzer.java`.
- Decisión: `backend/adaptive-quiz-domain/.../RuleBasedAdaptationStrategy.java`.
- Adaptación/persistencia: `backend/adaptive-quiz-application/.../AdaptationActionPersistenceService.java`.
- Orquestación: `backend/adaptive-quiz-application/.../PracticaService.java`.
- Visualización: `mobile/.../feature/monitor/MonitorScreen.kt`.
- Evidencia: `docs/08-evidence/ADAPTIVE_ENGINE_VALIDATION.md`.

## Mensaje técnico de cierre

> “El valor demostrable no es sólo cambiar una etiqueta visual: el sistema persiste el contexto, explica la regla, ejecuta una acción sobre el progreso y usa ese nuevo estado para la siguiente experiencia.”
