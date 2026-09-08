# Matriz maestra de cumplimiento — Taller 001

Esta matriz es la fuente de consulta para la entrega académica. Un estado **CUMPLIDO** exige una implementación y una evidencia local o remota identificable; **PREPARADO** indica material listo para una actividad que todavía no se ha ejecutado, y **PENDIENTE DE COMPROBACIÓN** evita afirmar una validación no realizada.

| Requisito del Taller | Implementación AdaptiveQuiz | Evidencia | Estado |
|---|---|---|---|
| **A. Aplicación móvil** | APK Android Kotlin/Compose con Inicio, Práctica, Resultado, Monitor Adaptativo y Progreso. | `docs/08-evidence/MOBILE_BUILD_VALIDATION.md`; `mobile/`. | **CUMPLIDO** |
| **A. Modifica automáticamente su comportamiento** | Ajusta dificultad y disponibilidad de pista al registrar un intento. | `RuleBasedAdaptationStrategy`; `ADAPTIVE_ENGINE_VALIDATION.md`. | **CUMPLIDO** |
| **A. Reacciona al contexto** | Usa aciertos, errores, tiempo, rachas y dificultad actual. | `LearningContextBuilder`; `ADP_CONTEXTO_APRENDIZAJE`; `END_TO_END_VALIDATION.md`. | **CUMPLIDO** |
| **B. Detecta al menos una variable de contexto real** | Detecta varias variables reales de intentos persistidos, incluido `tiempoRespuestaMs`. | `PRA_INTENTO`; `ADP_CONTEXTO_APRENDIZAJE`; `ADAPTIVE_ENGINE_VALIDATION.md`. | **CUMPLIDO** |
| **B. Procesa automáticamente los cambios** | `DefaultPerformanceAnalyzer` calcula precisión, promedio y rachas tras cada intento. | `DefaultPerformanceAnalyzer.java`; `ADAPTIVE_ENGINE_VALIDATION.md`. | **CUMPLIDO** |
| **B. Toma una decisión** | `RuleBasedAdaptationStrategy` evalúa reglas por prioridad y devuelve BAJO, MEDIO o ALTO. | `CFG_REGLA_ADAPTACION`; `04_DECISION_RULES.md`; eventos reales 1–14. | **CUMPLIDO** |
| **B. Modifica funcionalidad o comportamiento** | Ejecuta subir, mantener o bajar dificultad y activar pista. | `ADP_EVENTO_ADAPTACION`; `ADP_ACCION_EVENTO`; `END_TO_END_VALIDATION.md`. | **CUMPLIDO** |
| **B. Responde dinámicamente** | El siguiente ejercicio y la UI consumen el progreso que dejó la decisión anterior. | `PracticaService`; `GET /api/v1/ejercicios/siguiente`; `MOBILE_BUILD_VALIDATION.md`. | **CUMPLIDO** |
| **B. Separación CONTEXTO → PROCESAMIENTO → DECISIÓN → ADAPTACIÓN** | Builder, analizador, motor/estrategia y ejecutor son responsabilidades separadas. | `docs/04-adaptive-engine/01_ADAPTIVE_PIPELINE.md`; README; `MonitorScreen.kt`. | **CUMPLIDO** |
| **C. Separación de responsabilidades** | UI con ViewModel/Repositorio; API REST; aplicación; dominio; infraestructura; PostgreSQL. | `docs/03-architecture/01_ARCHITECTURE.md`; estructura `backend/` y `mobile/`. | **CUMPLIDO** |
| **C. Código modular y nombres comprensibles** | Módulos backend y clases explícitas: `PracticaService`, `LearningContextBuilder`, `DefaultPerformanceAnalyzer` y `MonitorScreen`. | Rutas de código en README y documento técnico. | **CUMPLIDO** |
| **C. Evita duplicación innecesaria** | Reglas parametrizadas y estrategia única de adaptación; la UI no replica umbrales. | `CFG_POLITICA_ADAPTACION`; `CFG_REGLA_ADAPTACION`; `RuleBasedAdaptationStrategy.java`. | **CUMPLIDO** |
| **C. Manejo correcto de eventos** | Cada decisión registra contexto, evento, acciones, motivo, versión y éxito. | Tablas `ADP_*`; `ADAPTIVE_ENGINE_VALIDATION.md`. | **CUMPLIDO** |
| **C. Adaptación observable y demostrable** | Monitor muestra el pipeline y Progreso muestra historial; hay evidencia de subida, bajada y pista. | `MonitorScreen.kt`; `END_TO_END_VALIDATION.md`. | **CUMPLIDO** |
| **C. Sin selector manual de dificultad** | La dificultad la resuelve el progreso y la política; la interfaz no la solicita. | `MOBILE_BUILD_VALIDATION.md`; `README.md`. | **CUMPLIDO** |
| **D. GitHub y código fuente completo** | Repositorio público con rama principal, tag y release académica. | [Repositorio](https://github.com/devfixexpress-debug/adaptive-quiz); [release v1.0.0](https://github.com/devfixexpress-debug/adaptive-quiz/releases/tag/v1.0.0). | **CUMPLIDO** |
| **D. Proyecto ejecutable e instrucciones** | README describe prerequisitos, PostgreSQL, backend, Android, health, Swagger y APK. | `README.md`; `.env.example`; `docker-compose.yml`. | **CUMPLIDO** |
| **D. Dependencias, historial y revisión** | Maven, Gradle Wrapper, workflows, CHANGELOG y commits versionados. | `backend/pom.xml`; `mobile/gradlew`; `.github/workflows/`; `CHANGELOG.md`. | **CUMPLIDO** |
| **E. Documento técnico: descripción, contexto, comportamiento, pipeline, arquitectura, tecnologías y código** | Documento técnico compacto con siete secciones exigidas. | `docs/07-delivery/01_TALLER_001_DOCUMENTO_TECNICO.md`. | **CUMPLIDO** |
| **E. Documento técnico máximo dos páginas** | Fuente compacta: 326 palabras y 43 líneas. | Nota de limitación en `FINAL_RELEASE_AUDIT.md`. | **PENDIENTE DE COMPROBACIÓN AL EXPORTAR** |
| **F. Presentación de 3 minutos** | Guion cronometrado para problema, solución, pipeline, arquitectura y cierre. | `docs/07-delivery/02_PRESENTATION_SCRIPT.md`. | **PREPARADO PARA SUSTENTACIÓN** |
| **F. Demo de 5 minutos** | Guion cronometrado con práctica, monitor, persistencia y evidencia de eventos reales. | `docs/07-delivery/03_DEMO_SCRIPT.md`. | **PREPARADO PARA SUSTENTACIÓN** |
| **F. Revisión técnica y reto técnico** | Preguntas y respuestas sobre contexto, reglas, arquitectura, persistencia y reproducibilidad. | `docs/07-delivery/04_TECHNICAL_CHALLENGE_PREP.md`. | **PREPARADO PARA SUSTENTACIÓN** |

## Lectura rápida para el docente

1. Empiece por el [README](../../README.md).
2. Consulte el [documento técnico](01_TALLER_001_DOCUMENTO_TECNICO.md) y los [diagramas](../../database/diagrams/).
3. Abra la [evidencia end-to-end](../08-evidence/END_TO_END_VALIDATION.md) y el [contrato REST](../05-api/01_API_CONTRACT.md).
4. Ejecute la demo con el guion de cinco minutos y revise el Monitor Adaptativo.

La IA no está implementada ni es necesaria para cumplir el Taller 001.
