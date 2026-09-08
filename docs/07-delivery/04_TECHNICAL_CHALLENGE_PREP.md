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

## Modificación de parámetros en vivo

La pantalla **Configuración adaptativa** está identificada como **Uso docente / demostración**. Administra política y umbrales persistidos; no expone un selector manual de dificultad para el estudiante.

### Caso 1: 80 % → 70 %

1. Inicio → icono **Configuración adaptativa**.
2. En **RENDIMIENTO ALTO**, cambiar **Precisión mínima para subir** de `80` a `70` %.
3. Pulsar **GUARDAR CAMBIOS**, revisar el resumen y pulsar **APLICAR**.
4. Registrar una nueva práctica/intento con una precisión que no alcance 80 % pero alcance 70 %, respetando el tiempo configurado.
5. Abrir **Monitor Adaptativo** y verificar la regla `R_ALTO`, el motivo y la adaptación resultante.

### Caso 2: 3 → 2 errores

1. En **REFUERZO**, cambiar **Errores consecutivos** de `3` a `2`.
2. Guardar y confirmar.
3. Registrar dos errores consecutivos y verificar `R_BAJO_RACHA` con pista si su interruptor está activo.

### Caso 3: ventana 5 → 3

1. En **POLÍTICA**, cambiar **Ventana de intentos** de `5` a `3`.
2. Guardar y confirmar.
3. El siguiente contexto construido por `LearningContextBuilder` utiliza hasta tres intentos recientes disponibles.

### Caso 4: restaurar Taller

1. Pulsar **RESTAURAR VALORES DEL TALLER**.
2. Confirmar la restauración.
3. Verificar ventana `5`, `R_ALTO` `0.80`/`20000`, precisión baja `0.40` y racha `3`.

La restauración no modifica intentos, contextos, eventos, progreso, ejercicios ni estudiantes.

### Plan B: Swagger

Si Android no está disponible, abrir `http://localhost:8080/swagger-ui/index.html` y usar la etiqueta **Configuración adaptativa**:

1. `GET /api/v1/configuracion-adaptativa` para leer el estado actual.
2. `PATCH /api/v1/configuracion-adaptativa/reglas/R_ALTO` con `{"porcentajeAciertoMin": 0.70}`.
3. `PATCH /api/v1/configuracion-adaptativa/reglas/R_BAJO_RACHA` con `{"rachaErroresMin": 2}`.
4. `PATCH /api/v1/configuracion-adaptativa/politica` con `{"tamanoVentanaIntentos": 3}`.
5. `POST /api/v1/configuracion-adaptativa/restaurar-taller` al terminar.

Las siguientes decisiones leen PostgreSQL de nuevo; no requieren recompilar ni reiniciar backend o Android.

## Cambios que se pueden explicar sin afirmar que ya están implementados

Si el docente plantea una variación, describa la ubicación responsable, no un cambio improvisado:

- **Cambiar umbral o ventana:** ya está implementado para los campos permitidos de la política activa y las reglas `R_ALTO`, `R_BAJO_PRECISION` y `R_BAJO_RACHA`; no se modifica la UI del estudiante.
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
