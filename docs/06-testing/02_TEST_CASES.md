# Casos de Prueba

| ID | Entrada | Esperado |
|---|---|---|
| TC-001 | solicitar siguiente ejercicio en BASICO | ejercicio BASICO |
| TC-002 | responder opción correcta | intento CORRECTO |
| TC-010 | 4/5 correctas, 15s promedio | contexto 0.80 |
| TC-011 | 0.80 y 15s | ALTO |
| TC-012 | ALTO + INTERMEDIO | AVANZADO |
| TC-013 | 3 errores consecutivos + INTERMEDIO | BASICO |
| TC-014 | regla baja | pista activada |
| TC-015 | contexto calculado | snapshot persistido |
| TC-016 | decisión ejecutada | evento + acción persistidos |
| TC-017 | adaptación | visible en monitor |
| TC-018 | AVANZADO + SUBIR | permanece AVANZADO |
| TC-019 | BASICO + BAJAR | permanece BASICO |
| TC-020 | IA no disponible | estrategia por reglas funciona |
