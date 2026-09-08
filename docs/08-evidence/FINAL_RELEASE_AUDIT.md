# Auditoría final de release — AdaptiveQuiz v1.0.0

**Fecha de auditoría:** 2026-09-08
**Alcance:** cierre académico, documental, visual y reproducible. No se modificaron código funcional, migraciones, dependencias, CI ni el tag de release.

## Checkpoint revisado

| Elemento | Resultado verificado |
|---|---|
| Rama principal auditada | `main` en `fcbcaf2` (`docs(release): mark v1.0.0 publication`). |
| Versión del proyecto | `VERSION = 1.0.0`; backend y Android declaran `1.0.0`. |
| Tag | `v1.0.0` anotado, resuelto al commit `f670373`. |
| Release remota | [AdaptiveQuiz v1.0.0](https://github.com/devfixexpress-debug/adaptive-quiz/releases/tag/v1.0.0), publicada, no borrador y no versión preliminar. |
| Inmutabilidad | No se movió el tag, no se reescribió el historial y no se hizo `force push`. |

El commit auditado de `main` está después del commit apuntado por el tag porque contiene la anotación documental de publicación. La release y el tag existentes se preservaron como checkpoint certificado.

## Estado Git y estructura

| Comprobación | Resultado |
|---|---|
| `git status --short` antes de editar | Sin cambios. |
| `git diff --check` antes de editar | Sin errores de espacios. |
| Remoto | `origin` apunta a `https://github.com/devfixexpress-debug/adaptive-quiz.git`. |
| Workflows existentes | `.github/workflows/backend-ci.yml` y `.github/workflows/android-ci.yml` presentes; no se recrearon ni modificaron. |
| Archivos de entrada | `README.md`, `VERSION`, `CHANGELOG.md`, `docker-compose.yml` y `.env.example` presentes. |
| Estructura | `backend/`, `mobile/`, `database/` y `docs/` presentes. |

## Certificación disponible

| Área | Evidencia existente |
|---|---|
| Backend | Spring Boot 1.0.0, health `UP`, OpenAPI y endpoints REST: [BACKEND_RUNTIME_VALIDATION.md](BACKEND_RUNTIME_VALIDATION.md). |
| Base de datos | PostgreSQL 17.11, Flyway V1 → V1.1 → V2 y seed: [DATABASE_VALIDATION.md](DATABASE_VALIDATION.md). |
| Motor adaptativo | Contextos, decisiones y acciones persistidos: [ADAPTIVE_ENGINE_VALIDATION.md](ADAPTIVE_ENGINE_VALIDATION.md). |
| Integración | Android real + backend real + PostgreSQL real: [END_TO_END_VALIDATION.md](END_TO_END_VALIDATION.md). |
| Android | Lint, APK y smoke en `emulator-5554`: [MOBILE_BUILD_VALIDATION.md](MOBILE_BUILD_VALIDATION.md). |

## CI remoto

La consulta a GitHub Actions confirmó ejecuciones completadas correctamente para el commit de `main` auditado:

- [Backend CI — éxito](https://github.com/devfixexpress-debug/adaptive-quiz/actions/runs/34256991473).
- [Android CI — éxito](https://github.com/devfixexpress-debug/adaptive-quiz/actions/runs/34256991453).

La release `v1.0.0` también conserva la certificación verde asociada a su commit `f670373`.

## Revalidación local de sólo lectura

| Comprobación | Resultado observado durante el cierre |
|---|---|
| `GET /actuator/health` | `UP`. |
| `GET /api-docs` | HTTP 200. |
| Contenedor PostgreSQL | Activo y saludable. |
| Historial Flyway | Versiones exitosas `1`, `1.1` y `2`. |
| Persistencia adaptativa | 14 intentos, 14 contextos, 14 eventos y 18 acciones. |

El archivo `.env` no está presente en este workspace de cierre. No se creó uno para la auditoría; el contenedor activo se consultó directamente en modo sólo lectura. Una clonación nueva sigue el procedimiento documentado: copiar `.env.example` a `.env` antes de usar Docker Compose.

## Bloqueos reales

No se encontró un bloqueo técnico para clonar, levantar PostgreSQL, arrancar Spring Boot, abrir `mobile/` en Android Studio ni revisar las evidencias. El documento técnico tiene 326 palabras y 43 líneas fuente; su condición visual de máximo dos páginas debe verificarse en el formato de exportación que utilice el docente, ya que este entorno no dispone de un renderizador de documentos instalado. Por honestidad, esa comprobación no se marca como ejecutada.

## Conclusión

El checkpoint funcional de AdaptiveQuiz v1.0.0 permanece preservado. La documentación de cierre ofrece una ruta reproducible, una matriz de cumplimiento, diagramas revisados y guiones explícitos para presentación, demo y revisión técnica. La IA continúa fuera del alcance de la entrega.
