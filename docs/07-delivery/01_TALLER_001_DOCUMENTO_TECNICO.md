# Taller 001 — Documento técnico

> Mantener este archivo en **máximo 2 páginas** al exportarlo.

## 1. Descripción de la aplicación
AdaptiveQuiz es una aplicación móvil de aprendizaje adaptativo. Registra el desempeño del
estudiante mientras resuelve ejercicios y modifica automáticamente la dificultad y las ayudas
de la siguiente experiencia. El problema que aborda es la secuencia rígida de ejercicios que no
considera diferencias de rendimiento.

## 2. Contexto utilizado
El contexto está formado por variables reales de interacción: aciertos, errores, tiempo de
respuesta, racha reciente, dificultad y tipo de ejercicio actuales.

## 3. Comportamiento adaptativo
Al finalizar un intento, el sistema construye una ventana de rendimiento. Si detecta rendimiento
alto, puede subir la dificultad; si detecta errores consecutivos o baja precisión, puede disminuirla
y habilitar una pista; en el resto de casos mantiene el nivel. La decisión es automática.

## 4. Pipeline
`CONTEXTO -> PROCESAMIENTO -> DECISIÓN -> ADAPTACIÓN`

- Contexto: `LearningContextBuilder`.
- Procesamiento: `PerformanceAnalyzer`.
- Decisión: `AdaptationEngine`.
- Adaptación: `AdaptationActionExecutor`.

## 5. Arquitectura / componentes
Mobile Android consume un backend Spring Boot con PostgreSQL. La arquitectura separa UI,
casos de uso, dominio, infraestructura y persistencia. El motor adaptativo se mantiene
independiente de UI e IA.

## 6. Tecnologías
Kotlin, Android, Jetpack Compose, Java, Spring Boot, PostgreSQL, Flyway, REST/OpenAPI,
Docker, GitHub, JUnit y GitHub Actions.

## 7. Ubicación del código relevante
| Elemento | Ubicación esperada |
|---|---|
| Captura del contexto | `mobile/.../adaptation/context/` |
| Procesamiento | `mobile/.../adaptation/PerformanceAnalyzer.kt` |
| Decisión | `mobile/.../adaptation/AdaptationEngine.kt` |
| Adaptación | `mobile/.../adaptation/AdaptationActionExecutor.kt` |
| Persistencia backend | `backend/...-infrastructure/` |
| Reglas | BD `CFG_REGLA_ADAPTACION` + repositorio |
| Evidencia | `ADP_CONTEXTO_APRENDIZAJE`, `ADP_EVENTO_ADAPTACION` |
