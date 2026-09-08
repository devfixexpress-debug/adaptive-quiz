# Requerimientos para Diseño

Perfil técnico a preservar durante la implementación.

## Modalidad
- Principalmente on-line e interactiva.
- La adaptación debe poder ejecutarse localmente en mobile.
- Backend para persistencia y sincronización.

## Magnitud
Taller académico con arquitectura extensible; sin multi-tenant en v1.

## Tiempo de respuesta
- Interacción de UI inmediata.
- Evaluación de reglas objetivo < 200 ms.
- API local objetivo < 500 ms en demo.

## Seguridad
- No contraseñas propias en v1.
- Estudiante demo identificado por código.
- Preparar interfaz para Identity futura sin acoplarla.

## Auditoría
- Registrar eventos adaptativos.
- Mantener timestamps y versión de registro.
- Persistir regla/política aplicada.

## Reversa
No se implementa reversa de intentos en v1; si se anula un intento en futuro deberá conservarse
la huella y recalcular progreso.

## Disponibilidad
La decisión adaptativa mínima debe funcionar sin IA y, de ser posible, sin backend durante la
demostración local.
