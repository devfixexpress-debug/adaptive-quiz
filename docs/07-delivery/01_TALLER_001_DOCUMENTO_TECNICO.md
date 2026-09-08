# Taller 001 — Documento técnico

> Mantener este archivo en **máximo 2 páginas** al exportarlo.

## 1. Descripción

AdaptiveQuiz es una aplicación móvil de práctica adaptativa. A partir de los intentos reales del
estudiante, ajusta automáticamente la dificultad y puede habilitar una pista. No existe selector
manual de nivel ni dependencia de IA.

## 2. Contexto y comportamiento

Cada intento registra corrección y tiempo de respuesta. El backend toma una ventana reciente de
cinco intentos, calcula precisión, tiempo promedio, rachas, dificultad y tipo actuales, y persiste
el snapshot. Las reglas configuradas por prioridad determinan el rendimiento:

- bajo: racha de errores >= 3 o precisión <= 0.40;
- alto: precisión >= 0.80 y tiempo promedio <= 20 000 ms;
- medio: fallback.

La estrategia sube, mantiene o baja dificultad, respeta los límites BASICO/AVANZADO y habilita
pista para desempeño bajo.

## 3. Pipeline

Intento → Contexto → Procesamiento → Decisión → Adaptación

- Contexto: backend/adaptive-quiz-application/.../LearningContextBuilder.java.
- Procesamiento: backend/adaptive-quiz-domain/.../DefaultPerformanceAnalyzer.java.
- Decisión: backend/adaptive-quiz-domain/.../RuleBasedAdaptationStrategy.java.
- Adaptación/persistencia: backend/adaptive-quiz-application/.../AdaptationActionPersistenceService.java.
- Orquestación transaccional: backend/adaptive-quiz-application/.../PracticaService.java.

Los resultados se conservan en PRA_*, APR_PROGRESO_TEMA, ADP_CONTEXTO_APRENDIZAJE,
ADP_EVENTO_ADAPTACION y ADP_ACCION_EVENTO.

## 4. Arquitectura y tecnologías

Android Kotlin/Jetpack Compose consume un backend Spring Boot modular
(domain, application, infrastructure y API) con PostgreSQL y Flyway. El API se documenta con
OpenAPI y expone Actuator. La UI usa ViewModel, StateFlow y Retrofit; sólo representa la decisión
recibida y no contiene reglas adaptativas.

La base se crea con V1, V1.1 y la migración aditiva V2. V1/V1.1 permanecen inmutables; Hibernate
valida el esquema, no lo genera.

## 5. Demostración

En PostgreSQL 17.11 real se verificaron catorce intentos, catorce contextos y catorce eventos. El evento 1
demostró INTERMEDIO → AVANZADO por R_ALTO; eventos posteriores demostraron reducción y
ACTIVAR_PISTA. El endpoint de detalle devuelve contexto, regla, motivo y acciones. El APK debug
compila, pasa lint y fue instalado en emulator-5554: Inicio, Práctica, Resultado, Monitor y
Progreso consumieron la API real. El teléfono físico también fue detectado por ADB.

## 6. Evidencia y alcance

Las evidencias reproducibles están en docs/08-evidence/. La certificación de esta iteración es
contra backend y PostgreSQL reales, sin introducir mocks ni nuevas carpetas de pruebas. Sólo se
soporta visualmente OPCION_UNICA; el cambio dinámico de tipo está modelado y queda fuera de la
demo principal. IA queda explícitamente fuera de alcance.
