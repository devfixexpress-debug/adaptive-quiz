# Guion de demo — 5 minutos

## Objetivo de la demo

Mostrar una práctica real y hacer visible la cadena **CONTEXTO → PROCESAMIENTO → DECISIÓN → ADAPTACIÓN**. La demo usa Android, Spring Boot y PostgreSQL reales; no usa datos simulados ni selector manual de dificultad.

## Prevuelo (antes de compartir pantalla)

1. Confirme `http://localhost:8080/actuator/health` con resultado `UP`.
2. Confirme que Android apunta a `http://10.0.2.2:8080/` en el emulador.
3. Abra la app en Inicio y deje accesibles el Monitor Adaptativo, Progreso y Swagger.
4. Mantenga los datos certificados tal como están; no ejecute comandos de reinicio de volumen ni modifique migraciones.

## Cronograma

| Tiempo | Acción | Qué debe observar el docente |
|---:|---|---|
| 0:00–0:35 | Abra Inicio, muestre Estudiante Demo, Matemática y Álgebra; pulse **COMENZAR PRÁCTICA**. | No hay selector manual BASICO/INTERMEDIO/AVANZADO. |
| 0:35–1:20 | Muestre la pregunta, opciones y dificultad devuelta por backend. Responda y confirme. | La app mide el tiempo automáticamente y envía el intento al API real. |
| 1:20–2:15 | Abra Resultado y luego **Monitor Adaptativo**. | Contexto, precisión, promedio, racha, rendimiento, regla, acción, dificultades y motivo. |
| 2:15–3:05 | Abra Progreso o Swagger con `GET /api/v1/estudiantes/1/adaptaciones`. | Historial persistido de decisiones, no una regla calculada por Compose. |
| 3:05–4:00 | Muestre los eventos certificados: alto `INTERMEDIO → AVANZADO`, bajo por precisión, bajo por racha y `ACTIVAR_PISTA`. | La adaptación alta, media/baja y la pista ya tienen evidencia real persistida. |
| 4:00–4:35 | Muestre en código `LearningContextBuilder`, `DefaultPerformanceAnalyzer`, `RuleBasedAdaptationStrategy` y `AdaptationActionPersistenceService`. | Responsabilidades separadas, sin lógica adaptativa en la UI. |
| 4:35–5:00 | Muestre README, release y evidencias. | Ejecución reproducible, CI y trazabilidad académica. |

## Evidencia que se debe señalar

| Caso | Evidencia persistida certificada |
|---|---|
| Alto | Evento 1: `R_ALTO`, `INTERMEDIO → AVANZADO`. |
| Bajo por precisión | Evento 3: `R_BAJO_PRECISION`, `AVANZADO → INTERMEDIO`, pista activada. |
| Bajo por racha | Evento 4: `R_BAJO_RACHA`, `INTERMEDIO → BASICO`, pista activada. |
| Límite básico | Eventos 5 y 6: la dificultad permanece en `BASICO`. |
| Medio | Eventos 2 y 7: `R_MEDIO` mantiene dificultad. |

Los identificadores y las filas verificadas están en `docs/08-evidence/ADAPTIVE_ENGINE_VALIDATION.md` y `docs/08-evidence/END_TO_END_VALIDATION.md`.

## Si ocurre una incidencia durante la demo

- **Health no responde:** verificar Docker, el backend y el archivo `.env`; no editar ni recrear migraciones.
- **Android no carga el catálogo:** confirmar que el backend está disponible y que el emulador usa `10.0.2.2`, no `localhost`.
- **La decisión actual no coincide con el ejemplo esperado:** mostrar el historial certificado en Monitor/Progreso o Swagger. La base actual conserva eventos reales de alto, bajo, medio y pista; no se deben borrar para forzar un resultado.
- **Falta tiempo:** priorizar Monitor Adaptativo, historial de adaptaciones y una evidencia persistida; son la demostración directa del Taller.

## Cierre sugerido

> “Cada adaptación mostrada proviene de un intento real, queda persistida en PostgreSQL y vuelve a la app como una decisión explicable.”
