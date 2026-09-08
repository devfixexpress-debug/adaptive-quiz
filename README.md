# AdaptiveQuiz

**Taller 001 — Desarrollo de una Aplicación Adaptativa**  
Curso: SI806 — Desarrollo Adaptativo e Integrado de Software  
Universidad Nacional de Ingeniería — 2026-II

## 1. Propósito

AdaptiveQuiz es una aplicación móvil de aprendizaje adaptativo que detecta automáticamente el
rendimiento del estudiante a partir de sus aciertos, errores, tiempos de respuesta y rachas
recientes. Con esta información construye un contexto de aprendizaje, calcula el nivel de
rendimiento y adapta automáticamente la dificultad y el tipo de ejercicio.

> AdaptiveQuiz **no pertenece a VAEF**. Se desarrolla dentro del workspace VELTIA y puede
> reutilizar el generador y estándares técnicos consolidados en VAEF, especialmente la
> modularidad del backend y la disciplina de datos.

## 2. Pipeline adaptativo obligatorio

```text
CONTEXTO -> PROCESAMIENTO -> DECISIÓN -> ADAPTACIÓN
```

Implementación prevista:

```text
Intentos recientes
      |
      v
LearningContext
      |
      v
PerformanceAnalyzer
      |
      v
AdaptationEngine
      |
      v
AdaptationDecision
      |
      +--> dificultad
      +--> tipo de ejercicio
      +--> pistas/ayudas
```

El núcleo evaluable del Taller será **determinista y basado en reglas**. La IA se incorporará
posteriormente como estrategia adicional, nunca como dependencia del comportamiento mínimo
exigido por el curso.

## 3. Arquitectura objetivo

- `mobile/`: Android, Kotlin, Jetpack Compose.
- `backend/`: Spring Boot modular, generado/reutilizado desde el generador VAEF.
- `database/`: PostgreSQL, Flyway, modelo robusto y paramétrico.
- `docs/`: requisitos, análisis, arquitectura, trazabilidad, pruebas, entrega.
- `docker/`: composición local.
- `.github/`: gestión, plantillas y CI.

## 4. Estructura

Consulte [`docs/README.md`](docs/README.md).

## 5. Modelo de datos

La arquitectura de datos separa:

1. **Catálogo / clasificación**: `CAT_*`.
2. **Configuración y parámetros**: `CFG_*`.
3. **Parque de datos académico y aprendizaje**: `ACA_*`, `BAN_*`, `APR_*`.
4. **Eventos operativos**: `PRA_*`.
5. **Contexto y eventos adaptativos**: `ADP_*`.

El DDL se encuentra en:

`database/migrations/postgresql/V1__adaptive_quiz_schema.sql`

El diccionario se encuentra en:

`database/DICCIONARIO_DATOS.md`

## 6. Ejecución esperada

### Base de datos
```powershell
Copy-Item .env.example .env
docker compose --env-file .env up -d db
```

### Backend
```powershell
# Desde adaptive-quiz/. La ruta absoluta permite que Spring lea el .env no versionado.
$env:ADAPTIVEQUIZ_ENV_FILE = (Resolve-Path .env).Path
Push-Location backend
mvn clean verify
mvn -pl adaptive-quiz-api -am spring-boot:run
```

Con el backend en ejecución:

~~~text
http://localhost:8080/actuator/health
http://localhost:8080/swagger-ui/index.html
~~~

### Mobile
Abrir `mobile/` en Android Studio y ejecutar el módulo `app`.

El corte M0/M1 deja el backend y PostgreSQL ejecutables. No se implementó ni modificó la aplicación Android en este corte.

## 7. Trazabilidad

Todo cambio debe seguir:

```text
Requisito -> Historia -> Issue -> Branch -> Commit -> PR -> Test -> Release
```

Matriz: `docs/01-requirements/04_TRACEABILITY_MATRIX.md`

## 8. Versionado

SemVer. Primera línea base documental y de datos: `v0.1.0`.

## 9. IA

La IA está planificada en `docs/09-ai-roadmap/`. El Taller 001 puede ser presentado y
demostrado completamente sin IA.
