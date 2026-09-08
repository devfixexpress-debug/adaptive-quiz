# AdaptiveQuiz

**Versión de entrega: 1.0.0**
**Taller 001 — Desarrollo de una Aplicación Adaptativa**  
Curso: SI806 — Desarrollo Adaptativo e Integrado de Software  
Universidad Nacional de Ingeniería — 2026-II

## Propósito

AdaptiveQuiz es una aplicación móvil de aprendizaje adaptativo. Registra aciertos, errores,
tiempos y rachas; construye un contexto de aprendizaje y modifica automáticamente la siguiente
experiencia. El estudiante no elige manualmente un nivel de dificultad.

AdaptiveQuiz es independiente de VAEF. Comparte el workspace VELTIA y reutiliza sus patrones
técnicos consolidados (módulos, Spring Boot, Flyway, OpenAPI y manejo de errores), pero no tiene
dependencias funcionales con sus servicios.

## Pipeline adaptativo implementado

```text
Intento real
  → LearningContextBuilder
  → PerformanceAnalyzer
  → RuleBasedAdaptationStrategy
  → AdaptationActionExecutor
  → contexto, evento y acciones persistidos
  → siguiente experiencia
```

La estrategia activa es determinista y parametrizada en `CFG_POLITICA_ADAPTACION` y
`CFG_REGLA_ADAPTACION`. Aplica, por prioridad, las reglas `R_BAJO_RACHA`,
`R_BAJO_PRECISION`, `R_ALTO` y el fallback `R_MEDIO`; respeta los límites de `BASICO` y
`AVANZADO`. `AiAdaptationStrategy` sólo está prevista por el contrato `AdaptationStrategy`: no
hay SDK, clave ni integración de IA.

## Arquitectura

- `backend/adaptive-quiz-domain`: modelos, puertos y contratos puros de adaptación.
- `backend/adaptive-quiz-application`: casos de uso, contexto, motor y ejecución de acciones.
- `backend/adaptive-quiz-infrastructure`: JPA/JDBC, mappers y adaptadores PostgreSQL.
- `backend/adaptive-quiz-api`: Spring Boot, REST, Flyway, OpenAPI y Actuator.
- `mobile`: Android Kotlin con Jetpack Compose, Material 3, ViewModel, StateFlow y Retrofit.
- `database`: fuente canónica de DDL, seed y migraciones.

La fuente canónica de datos se conserva en `database/`. El API empaqueta las migraciones para
Flyway; Hibernate usa `ddl-auto=validate`, nunca genera el esquema.

## Requisitos locales

- Docker Desktop.
- JDK 17.
- Maven 3.9 o compatible.
- Android Studio con Android SDK Platform 37 para el build Android actual.
- Git para clonar el repositorio y conservar el Gradle Wrapper versionado.

## Ejecución con PostgreSQL real

Para clonar y arrancar desde cero:

```powershell
git clone https://github.com/devfixexpress-debug/adaptive-quiz.git
Set-Location adaptive-quiz
Copy-Item .env.example .env
docker compose --env-file .env up -d db

$env:ADAPTIVEQUIZ_ENV_FILE = (Resolve-Path .env).Path
Push-Location backend
mvn clean verify
java -jar adaptive-quiz-api/target/adaptive-quiz-api-1.0.0.jar
```

El ejemplo local expone PostgreSQL en `55432` y el backend en `8080`. Las credenciales de
desarrollo viven sólo en `.env`; no versionar una variante con secretos reales.

Comprobaciones de runtime:

```text
http://localhost:8080/actuator/health
http://localhost:8080/api-docs
http://localhost:8080/swagger-ui/index.html
```

## API disponible

```text
GET  /api/v1/asignaturas
GET  /api/v1/asignaturas/{id}/temas
GET  /api/v1/ejercicios/{id}
POST /api/v1/sesiones-practica
GET  /api/v1/ejercicios/siguiente?estudianteId={id}&temaId={id}
POST /api/v1/intentos
GET  /api/v1/estudiantes/{id}/progreso
GET  /api/v1/estudiantes/{id}/progreso/{temaId}
GET  /api/v1/estudiantes/{id}/adaptaciones
GET  /api/v1/adaptaciones/{id}
```

## Android

Abra `mobile/` en Android Studio o ejecute:

```powershell
Push-Location mobile
$env:ANDROID_HOME = "$env:LOCALAPPDATA\\Android\\Sdk"
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
.\\gradlew.bat lintDebug assembleDebug
```

El APK se genera en `mobile/app/build/outputs/apk/debug/app-debug.apk`. En emulador, la URL
por defecto es `http://10.0.2.2:8080/`; se centraliza en
`adaptiveQuizApiBaseUrl` de `mobile/gradle.properties` y se entrega mediante `BuildConfig`.
Para un dispositivo físico sólo se modifica esa propiedad.

Las pantallas son Inicio, Práctica, Resultado, Monitor Adaptativo y Progreso. El Monitor muestra
visiblemente `CONTEXTO → PROCESAMIENTO → DECISIÓN → ADAPTACIÓN`; Compose sólo representa la
decisión recibida del API y no contiene reglas de negocio.

## Pruebas y calidad local

El release combina pruebas unitarias deterministas del dominio/aplicación con la certificación
E2E ya registrada contra Spring Boot y PostgreSQL reales. Para repetir los builds locales:

~~~powershell
Push-Location backend
mvn clean verify
mvn package
Pop-Location

Push-Location mobile
.\gradlew.bat clean lintDebug assembleDebug
Pop-Location
~~~

El APK debug resultante es
`mobile/app/build/outputs/apk/debug/app-debug.apk`. No se usa Testcontainers como evidencia
principal: la evidencia de integración se conserva en `docs/08-evidence/`.

## Integración continua

Los workflows versionados se ejecutan en cada `push` y `pull request` hacia `main`:

- `.github/workflows/backend-ci.yml`: JDK 17 y `mvn -B clean verify`.
- `.github/workflows/android-ci.yml`: JDK 17, Android SDK Platform 37,
  `lintDebug` y `assembleDebug`.

El tag y la release `v1.0.0` se crearán únicamente después de que ambos workflows estén verdes
en GitHub Actions.

## Demostración reproducible

1. Inicie una sesión para Estudiante Demo y Álgebra con `POST /sesiones-practica`.
2. Obtenga la pregunta con `GET /ejercicios/siguiente`.
3. Registre una respuesta correcta y rápida mediante `POST /intentos`: desde un esquema limpio,
   el inicio es `INTERMEDIO` y la regla `R_ALTO` lleva a `AVANZADO`.
4. Registre tres errores consecutivos: se aplica `R_BAJO_PRECISION` o `R_BAJO_RACHA`, baja la
   dificultad y se persiste `ACTIVAR_PISTA`.
5. Consulte `/estudiantes/1/adaptaciones` o el Monitor Adaptativo para ver el contexto, regla,
   motivo, dificultad anterior/nueva y acciones.

La validación de esta iteración se realiza directamente contra backend y PostgreSQL reales. Las
evidencias, IDs y comandos ejecutados están en `docs/08-evidence/`.

## Versionado, trazabilidad y entrega

Versión actual: `1.0.0`.

- Matriz: `docs/01-requirements/04_TRACEABILITY_MATRIX.md`.
- Motor: `docs/04-adaptive-engine/`.
- Validación: `docs/08-evidence/ADAPTIVE_ENGINE_VALIDATION.md`,
  `MOBILE_BUILD_VALIDATION.md` y `END_TO_END_VALIDATION.md`.
- Documento técnico de entrega: `docs/07-delivery/01_TALLER_001_DOCUMENTO_TECNICO.md`.
- Checklist de entrega: `docs/07-delivery/05_DELIVERY_CHECKLIST.md`.

## IA

La IA sigue fuera del alcance de esta entrega. El Taller 001 funciona íntegramente con
`RuleBasedAdaptationStrategy`.
