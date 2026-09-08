# Criterios de Aceptación

## CA-001 — Captura
Dado un ejercicio respondido, cuando se confirma la respuesta, entonces el intento registra:
resultado, tiempo y dificultad sin pedir esos valores al usuario.

## CA-002 — Rendimiento alto
Dada una ventana suficiente de intentos, cuando la precisión sea alta y el tiempo promedio
cumpla la política, entonces el rendimiento será ALTO.

## CA-003 — Subida
Dado rendimiento ALTO y dificultad menor a AVANZADO, el siguiente ejercicio deberá usar el
siguiente nivel de dificultad.

## CA-004 — Errores consecutivos
Dados tres errores consecutivos, el motor deberá priorizar una regla de dificultad y podrá
disminuir el nivel.

## CA-005 — Límite superior
Dado nivel AVANZADO y decisión SUBIR_DIFICULTAD, el nivel permanecerá AVANZADO.

## CA-006 — Límite inferior
Dado nivel BASICO y decisión BAJAR_DIFICULTAD, el nivel permanecerá BASICO.

## CA-007 — Explicabilidad
Toda adaptación debe registrar contexto, rendimiento, acción, valor anterior, valor nuevo y
motivo.

## CA-008 — Separación
La UI no contiene los umbrales del motor y el motor no depende de componentes visuales.

## CA-009 — Demo
Desactivando cualquier mecanismo manual de nivel, la secuencia de respuestas de la demo debe
producir al menos una subida o bajada automática visible.

## CA-010 — IA desacoplada
La aplicación debe ejecutar el flujo adaptativo completo con IA deshabilitada.
