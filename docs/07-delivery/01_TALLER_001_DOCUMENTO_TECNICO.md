# Taller 001 — Documento técnico

> Mantener este archivo en **máximo 2 páginas** al exportarlo.

## 1. Descripción

AdaptiveQuiz es una aplicación móvil de práctica adaptativa para el Taller 001 de SI806. A partir
de respuestas reales, ajusta automáticamente la siguiente experiencia de aprendizaje. El
estudiante no selecciona manualmente dificultad y la solución no depende de IA.

## 2. Contexto

Cada intento registra respuesta, corrección y tiempo. El backend construye una ventana móvil de
hasta cinco intentos recientes disponibles, según `tamano_ventana_intentos` de la política, y
persiste el snapshot con precisión, tiempo promedio, rachas, dificultad y tipo de ejercicio.

## 3. Comportamiento adaptativo

Las reglas parametrizadas se evalúan por prioridad: bajo desempeño por racha de errores >= 3 o
precisión <= 0.40; alto desempeño por precisión >= 0.80 y tiempo promedio <= 20 000 ms; y
fallback medio. La decisión sube, mantiene o baja dificultad, respeta los límites
BASICO/AVANZADO y habilita pista para desempeño bajo.

La certificación contra PostgreSQL 17.11, Spring Boot y Android real verificó subidas,
reducciones y `ACTIVAR_PISTA`, con eventos, contexto y acciones persistidos.

## 4. Pipeline

Intento → Contexto → Procesamiento → Decisión → Adaptación

- Contexto: `LearningContextBuilder`.
- Procesamiento: `DefaultPerformanceAnalyzer`.
- Decisión: `RuleBasedAdaptationStrategy`.
- Adaptación/persistencia: `AdaptationActionPersistenceService`.
- Orquestación transaccional: `PracticaService`.

Los resultados se conservan en PRA_*, `APR_PROGRESO_TEMA`,
`ADP_CONTEXTO_APRENDIZAJE`, `ADP_EVENTO_ADAPTACION` y
`ADP_ACCION_EVENTO`.

## 5. Arquitectura y componentes

Android consume un backend Spring Boot modular: domain (modelo y contratos), application (casos
de uso y motor), infrastructure (persistencia PostgreSQL) y API (REST, Flyway, OpenAPI y
Actuator). La UI usa ViewModel y sólo representa la decisión recibida; no contiene reglas
adaptativas. V1, V1.1 y V2 son migraciones inmutables y Hibernate valida el esquema.

## 6. Tecnologías

Kotlin, Jetpack Compose, Material 3, Retrofit, StateFlow; Java 17, Spring Boot, Maven, JPA,
Flyway, PostgreSQL 17.11, Docker, OpenAPI y Actuator.

## 7. Ubicación de código relevante

- Backend: `backend/adaptive-quiz-domain`, `adaptive-quiz-application`,
  `adaptive-quiz-infrastructure` y `adaptive-quiz-api`.
- Motor: `backend/adaptive-quiz-domain/.../adaptation/` y
  `backend/adaptive-quiz-application/.../service/`.
- Android: `mobile/app/src/main/java/com/veltia/adaptivequiz/mobile/`.
- Migraciones canónicas: `database/migrations/postgresql/`.
- Evidencia de ejecución: `docs/08-evidence/`.
