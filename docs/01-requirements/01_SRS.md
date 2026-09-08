# Especificación de Requisitos de Software (SRS)

## Actores
- Estudiante.
- Docente/revisor (observa la demo).
- Administrador de contenido (opcional en v1).

## Requisitos funcionales
- **RF-001** Listar asignaturas y temas disponibles.
- **RF-002** Obtener el siguiente ejercicio aplicable al estudiante.
- **RF-003** Presentar un ejercicio y sus opciones.
- **RF-004** Registrar la respuesta del estudiante.
- **RF-005** Calificar automáticamente ejercicios de opción única.
- **RF-006** Registrar cada intento con tiempo de respuesta.
- **RF-007** Consultar progreso por tema.
- **RF-008** Consultar historial de eventos adaptativos.
- **RF-009** Persistir y recuperar banco de ejercicios.
- **RF-010** Exponer API REST para mobile.

## Requisitos adaptativos
- **RA-001** Capturar automáticamente aciertos y errores.
- **RA-002** Capturar automáticamente el tiempo de respuesta.
- **RA-003** Calcular una ventana de rendimiento reciente.
- **RA-004** Determinar nivel de rendimiento.
- **RA-005** Aumentar automáticamente la dificultad.
- **RA-006** Mantener la dificultad cuando corresponda.
- **RA-007** Disminuir automáticamente la dificultad.
- **RA-008** Habilitar una pista como acción adaptativa.
- **RA-009** Poder cambiar el tipo de ejercicio según política.
- **RA-010** Registrar el contexto que originó la decisión.
- **RA-011** Registrar qué regla y qué acción fueron aplicadas.
- **RA-012** Mostrar en pantalla una adaptación observable.

## Requisitos no funcionales
- **RNF-001 Modularidad:** separar contexto, procesamiento, decisión y adaptación.
- **RNF-002 Testabilidad:** reglas evaluables sin UI ni red.
- **RNF-003 Mantenibilidad:** reglas y umbrales configurables.
- **RNF-004 Explicabilidad:** toda decisión debe indicar motivo y evidencia.
- **RNF-005 Rendimiento:** decisión local en menos de 200 ms en condiciones normales.
- **RNF-006 Integridad:** PK/FK/UK/CHECK en la base.
- **RNF-007 Portabilidad:** backend Spring Boot y PostgreSQL; diseño no dependiente de IA.
- **RNF-008 Observabilidad:** eventos adaptativos persistidos.
- **RNF-009 Seguridad mínima:** no almacenar contraseñas; identidad simplificada para demo.
- **RNF-010 Usabilidad:** interfaz simple y en español.

## Requisito futuro de IA
- **RFAI-001** El motor deberá aceptar una estrategia IA sin modificar el contrato del motor.
- **RFAI-002** La estrategia IA no podrá ser requerida para cumplir RA-001..RA-012.
