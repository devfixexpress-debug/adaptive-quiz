# Validación end-to-end M3/M4/M5/M6

Fecha de ejecución: 2026-09-08.

## Topología real

~~~text
Emulador Android emulator-5554
   ↓ Retrofit HTTP
Spring Boot AdaptiveQuiz :8080
   ↓ JDBC/JPA + Flyway
PostgreSQL 17.11 Docker :55432
~~~

No se utilizó base simulada, controlador simulado ni cliente HTTP simulado.

## Comprobaciones de backend

| Recurso | Resultado |
|---|---|
| GET /actuator/health | UP |
| GET /api-docs | HTTP 200 |
| GET /swagger-ui/index.html | HTTP 200 |
| GET /api/v1/asignaturas | Matemática real |
| GET /api/v1/asignaturas/1/temas | Álgebra real |
| GET /api/v1/ejercicios/1 | ejercicio, opciones y pista reales |
| POST /api/v1/sesiones-practica | sesiones 2–4 creadas |
| GET /api/v1/ejercicios/siguiente | ALG-B-001, dificultad y pista reales |
| POST /api/v1/intentos | intentos 6–14 persistidos |
| GET progreso/adaptaciones/detalle | datos y relaciones persistidas |

## Flujo Android observado

Inicio cargó el catálogo desde la API. La práctica no ofreció selector manual de dificultad:
mostró la dificultad devuelta por el progreso. Tras la opción correcta, Resultado consumió la
decisión real y Monitor mostró:

~~~text
CONTEXTO: precisión 60 %, tiempo promedio 21.0 s, racha 3 aciertos
PROCESAMIENTO: MEDIO, puntaje 0.450000
DECISIÓN: R_MEDIO, MANTENER_DIFICULTAD
ADAPTACIÓN: BASICO → BASICO
~~~

La pantalla Progreso mostró el historial incluyendo R_BAJO_RACHA, R_BAJO_PRECISION y R_MEDIO.
La sesión interactiva continuó con eventos 8 y 9, demostrando BASICO → INTERMEDIO → AVANZADO.

## Pruebas de adaptación ya persistidas

- Alto: evento 1, INTERMEDIO → AVANZADO con R_ALTO.
- Bajo por precisión: evento 3, AVANZADO → INTERMEDIO con ACTIVAR_PISTA.
- Bajo por racha: evento 4, INTERMEDIO → BASICO con ACTIVAR_PISTA.
- Límite básico: eventos 5/6 mantuvieron BASICO aunque la acción principal fue bajar.
- Fallback medio: eventos 2 y 7 mantuvieron la dificultad.

## Resultado

El vertical slice completo está demostrado: Android real + backend real + PostgreSQL real +
14 contextos/decisiones y 18 acciones persistidas. La IA sigue fuera de alcance. Para repetir desde un
estado inicial se debe crear un volumen de base de datos nuevo deliberadamente; esta evidencia no
elimina ni reinicia datos de demostración existentes.
