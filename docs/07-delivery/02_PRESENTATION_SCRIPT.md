# Guion de presentación — 3 minutos

## Objetivo

Explicar en tres minutos que AdaptiveQuiz es una aplicación móvil que **observa un contexto real, lo procesa, decide y adapta la siguiente experiencia**. La presentación no sustituye la demo: prepara al docente para reconocer qué debe observar en ella.

## Preparación antes de hablar

- Tener abierto el README en la sección **Pipeline adaptativo**.
- Tener lista la app Android en Inicio o Monitor Adaptativo.
- Mantener una pestaña de Swagger o de las evidencias para la demo posterior.
- No reiniciar ni alterar la base de datos certificada.

## Cronograma

| Tiempo | Mensaje | Apoyo visual |
|---:|---|---|
| 0:00–0:30 | **Problema.** Una secuencia rígida trata igual a estudiantes con desempeños distintos. | README: Problema. |
| 0:30–1:00 | **Solución.** AdaptiveQuiz observa aciertos, errores, tiempo, rachas y dificultad actual. El estudiante no selecciona el nivel. | Inicio o Práctica de Android. |
| 1:00–1:40 | **Pipeline.** Después de un intento, el backend construye contexto, calcula rendimiento, selecciona una regla y ejecuta una adaptación. | Diagrama Mermaid del README o `02_pipeline_adaptativo.mmd`. |
| 1:40–2:15 | **Decisión explicable.** Las reglas son deterministas, parametrizadas y priorizadas: bajo desempeño refuerza; alto desempeño eleva el reto; el caso restante mantiene. | `04_DECISION_RULES.md` o Monitor Adaptativo. |
| 2:15–2:45 | **Arquitectura.** Android representa el resultado; el backend concentra el motor; PostgreSQL conserva contexto, eventos y acciones. | Diagrama de arquitectura del README. |
| 2:45–3:00 | **Cierre.** La adaptación es observable, persistida y demostrable sin IA. La demo muestra el flujo completo. | Monitor Adaptativo / matriz de cumplimiento. |

## Frase de cierre sugerida

> “AdaptiveQuiz no pide al estudiante escoger una dificultad: la deduce de evidencia real, explica la decisión y conserva la trazabilidad de la adaptación.”

## Límites que deben declararse si se preguntan

- La IA está fuera del alcance de v1.0.0; el Taller se cumple con `RuleBasedAdaptationStrategy`.
- La demostración certificada es local con Android, Spring Boot y PostgreSQL reales; no se afirma un despliegue externo.
- El tipo obligatorio de la demo es `OPCION_UNICA`; el modelo conserva extensibilidad sin afirmar capacidades no demostradas.
