# Diccionario de Datos

## Convenciones comunes
- PK: identificador técnico `BIGINT`.
- Fechas: `TIMESTAMPTZ`.
- Auditoría: `fecha_creacion`, `usuario_creacion`, `fecha_modificacion`, `usuario_modificacion`, `version_registro`.

---

## CAT_CATALOGO
Catálogo genérico de clasificaciones paramétricas.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_catalogo | BIGINT | No | PK técnica |
| codigo | VARCHAR(50) | No | Código semántico único |
| nombre | VARCHAR(120) | No | Nombre |
| descripcion | VARCHAR(500) | Sí | Alcance |
| activo | BOOLEAN | No | Vigencia operativa |
| fecha_creacion | TIMESTAMPTZ | No | Auditoría |
| usuario_creacion | VARCHAR(100) | No | Auditoría |
| fecha_modificacion | TIMESTAMPTZ | Sí | Auditoría |
| usuario_modificacion | VARCHAR(100) | Sí | Auditoría |
| version_registro | BIGINT | No | Control optimista |

## CAT_ITEM_CATALOGO
Valor permitido dentro de un catálogo.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_item_catalogo | BIGINT | No | PK |
| id_catalogo | BIGINT | No | Catálogo dueño |
| id_item_padre | BIGINT | Sí | Jerarquía opcional |
| codigo | VARCHAR(50) | No | Código único dentro del catálogo |
| nombre | VARCHAR(120) | No | Etiqueta |
| descripcion | VARCHAR(500) | Sí | Definición |
| orden | INTEGER | No | Orden semántico/visual |
| activo | BOOLEAN | No | Vigencia |
| fecha_creacion | TIMESTAMPTZ | No | Auditoría |
| usuario_creacion | VARCHAR(100) | No | Auditoría |
| fecha_modificacion | TIMESTAMPTZ | Sí | Auditoría |
| usuario_modificacion | VARCHAR(100) | Sí | Auditoría |
| version_registro | BIGINT | No | Control optimista |

## CFG_PARAMETRO
Parámetros estables/configurables del motor.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_parametro | BIGINT | No | PK |
| codigo | VARCHAR(80) | No | Código único |
| nombre | VARCHAR(150) | No | Nombre |
| descripcion | VARCHAR(500) | Sí | Uso |
| tipo_dato | VARCHAR(20) | No | ENTERO/DECIMAL/BOOLEANO/TEXTO |
| valor_entero | BIGINT | Sí | Valor entero |
| valor_decimal | NUMERIC(18,6) | Sí | Valor decimal |
| valor_booleano | BOOLEAN | Sí | Valor booleano |
| valor_texto | VARCHAR(1000) | Sí | Valor texto |
| vigente_desde | TIMESTAMPTZ | Sí | Inicio de vigencia |
| vigente_hasta | TIMESTAMPTZ | Sí | Fin de vigencia |
| activo | BOOLEAN | No | Vigencia operativa |
| ...auditoría |  |  | Campos comunes |

## CFG_POLITICA_ADAPTACION
Versión de comportamiento adaptativo.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_politica_adaptacion | BIGINT | No | PK |
| codigo | VARCHAR(60) | No | Código lógico |
| nombre | VARCHAR(150) | No | Nombre |
| descripcion | VARCHAR(500) | Sí | Objetivo |
| version_politica | INTEGER | No | Versión funcional |
| id_item_modo_adaptacion | BIGINT | No | REGLAS/IA/HIBRIDO |
| tamano_ventana_intentos | SMALLINT | No | N intentos recientes |
| vigente_desde | TIMESTAMPTZ | No | Inicio |
| vigente_hasta | TIMESTAMPTZ | Sí | Fin |
| activa | BOOLEAN | No | Política seleccionable |
| ...auditoría |  |  | Campos comunes |

## CFG_REGLA_ADAPTACION
Regla paramétrica evaluada por prioridad.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_regla_adaptacion | BIGINT | No | PK |
| id_politica_adaptacion | BIGINT | No | Política |
| codigo | VARCHAR(60) | No | Código |
| nombre | VARCHAR(150) | No | Nombre |
| prioridad | INTEGER | No | Menor = primero |
| porcentaje_acierto_min | NUMERIC(5,4) | Sí | Límite inferior |
| porcentaje_acierto_max | NUMERIC(5,4) | Sí | Límite superior |
| tiempo_promedio_min_ms | INTEGER | Sí | Tiempo mínimo |
| tiempo_promedio_max_ms | INTEGER | Sí | Tiempo máximo |
| racha_aciertos_min | SMALLINT | Sí | Consistencia positiva |
| racha_errores_min | SMALLINT | Sí | Consistencia negativa |
| id_item_nivel_rendimiento | BIGINT | No | BAJO/MEDIO/ALTO |
| id_item_accion_principal | BIGINT | No | Acción |
| id_item_tipo_ejercicio_destino | BIGINT | Sí | Cambio opcional |
| habilitar_pista | BOOLEAN | No | Acción complementaria |
| activa | BOOLEAN | No | Vigencia |
| ...vigencia/auditoría |  |  | Control temporal |

## ACA_ASIGNATURA
Definición de asignatura.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_asignatura | BIGINT | No | PK |
| codigo | VARCHAR(40) | No | Código único |
| nombre | VARCHAR(150) | No | Nombre |
| descripcion | VARCHAR(500) | Sí | Alcance |
| activo | BOOLEAN | No | Vigencia |
| ...auditoría |  |  | Campos comunes |

## ACA_TEMA
Tema/subtema académico.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_tema | BIGINT | No | PK |
| id_asignatura | BIGINT | No | Asignatura |
| id_tema_padre | BIGINT | Sí | Jerarquía |
| codigo | VARCHAR(50) | No | Código dentro de asignatura |
| nombre | VARCHAR(150) | No | Nombre |
| descripcion | VARCHAR(500) | Sí | Descripción |
| orden | INTEGER | No | Secuencia |
| activo | BOOLEAN | No | Vigencia |
| ...auditoría |  |  | Campos comunes |

## BAN_EJERCICIO
Definición reusable de ejercicio.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_ejercicio | BIGINT | No | PK |
| id_tema | BIGINT | No | Tema |
| codigo | VARCHAR(60) | No | Código único |
| enunciado | TEXT | No | Pregunta/problema |
| id_item_tipo_ejercicio | BIGINT | No | Clasificación |
| id_item_dificultad | BIGINT | No | BASICO/INTERMEDIO/AVANZADO |
| id_item_estado_ejercicio | BIGINT | No | Estado |
| explicacion | TEXT | Sí | Explicación post respuesta |
| tiempo_objetivo_segundos | INTEGER | Sí | Referencia pedagógica |
| puntaje_base | NUMERIC(8,2) | No | Puntaje |
| ...auditoría |  |  | Campos comunes |

## BAN_OPCION_EJERCICIO
Opción de ejercicio.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_opcion_ejercicio | BIGINT | No | PK |
| id_ejercicio | BIGINT | No | Ejercicio |
| codigo | VARCHAR(20) | No | A/B/C/D |
| texto | TEXT | No | Contenido |
| es_correcta | BOOLEAN | No | Clave |
| orden | INTEGER | No | Orden |
| retroalimentacion | TEXT | Sí | Feedback |
| ...auditoría |  |  | Campos comunes |

## BAN_PISTA_EJERCICIO
Pista ordenada de un ejercicio.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_pista_ejercicio | BIGINT | No | PK |
| id_ejercicio | BIGINT | No | Ejercicio |
| orden | INTEGER | No | Nivel de pista |
| texto | TEXT | No | Ayuda |
| penalizacion_puntaje | NUMERIC(8,2) | No | Penalización |
| ...auditoría |  |  | Campos comunes |

## APR_ESTUDIANTE
Personaje que realiza la práctica.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_estudiante | BIGINT | No | PK |
| codigo | VARCHAR(50) | No | Identificador de negocio/demo |
| nombre_mostrado | VARCHAR(150) | No | Nombre visible |
| id_item_estado_estudiante | BIGINT | No | Estado |
| ...auditoría |  |  | Campos comunes |

## APR_PROGRESO_TEMA
Estado agregado actual del estudiante por tema.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_progreso_tema | BIGINT | No | PK |
| id_estudiante | BIGINT | No | Estudiante |
| id_tema | BIGINT | No | Tema |
| id_item_dificultad_actual | BIGINT | No | Nivel actual |
| total_intentos | INTEGER | No | Contador |
| total_aciertos | INTEGER | No | Contador |
| porcentaje_acierto | NUMERIC(5,4) | No | Resumen |
| tiempo_promedio_ms | INTEGER | No | Resumen |
| racha_aciertos_actual | SMALLINT | No | Estado |
| racha_errores_actual | SMALLINT | No | Estado |
| fecha_ultimo_intento | TIMESTAMPTZ | Sí | Última actividad |
| ...auditoría |  |  | Campos comunes |

## PRA_SESION_PRACTICA
Sesión real de práctica.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_sesion_practica | BIGINT | No | PK |
| id_estudiante | BIGINT | No | Estudiante |
| id_tema | BIGINT | No | Tema |
| id_politica_adaptacion | BIGINT | No | Política usada |
| id_item_estado_sesion | BIGINT | No | Estado |
| fecha_inicio | TIMESTAMPTZ | No | Inicio |
| fecha_fin | TIMESTAMPTZ | Sí | Fin |
| cantidad_intentos | INTEGER | No | Conteo |
| ...auditoría |  |  | Campos comunes |

## PRA_INTENTO
Evento real de resolución.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_intento | BIGINT | No | PK |
| id_sesion_practica | BIGINT | No | Sesión |
| id_ejercicio | BIGINT | No | Ejercicio resuelto |
| numero_orden | INTEGER | No | Secuencia |
| fecha_inicio | TIMESTAMPTZ | No | Presentación |
| fecha_fin | TIMESTAMPTZ | No | Confirmación |
| tiempo_respuesta_ms | INTEGER | No | Contexto real |
| id_item_resultado_intento | BIGINT | No | CORRECTO/INCORRECTO/OMITIDO |
| puntaje_obtenido | NUMERIC(8,2) | No | Resultado |
| uso_pista | BOOLEAN | No | Evidencia |
| ...auditoría |  |  | Campos comunes |

## PRA_RESPUESTA
Respuesta emitida para un intento.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_respuesta | BIGINT | No | PK |
| id_intento | BIGINT | No | Intento, único |
| texto_respuesta | TEXT | Sí | Para tipo abierto futuro |
| fecha_respuesta | TIMESTAMPTZ | No | Momento |
| ...auditoría |  |  | Campos comunes |

## PRA_RESPUESTA_OPCION
Opciones seleccionadas por una respuesta.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_respuesta | BIGINT | No | FK/PK |
| id_opcion_ejercicio | BIGINT | No | FK/PK |

## ADP_CONTEXTO_APRENDIZAJE
Snapshot de contexto usado por el motor.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_contexto_aprendizaje | BIGINT | No | PK |
| id_intento_disparador | BIGINT | No | Intento que dispara |
| id_estudiante | BIGINT | No | Estudiante |
| id_tema | BIGINT | No | Tema |
| id_politica_adaptacion | BIGINT | No | Política |
| numero_intentos_ventana | SMALLINT | No | N usados |
| total_aciertos_ventana | SMALLINT | No | Aciertos |
| porcentaje_acierto | NUMERIC(5,4) | No | Precisión |
| tiempo_promedio_ms | INTEGER | No | Tiempo |
| racha_aciertos | SMALLINT | No | Racha |
| racha_errores | SMALLINT | No | Racha |
| id_item_dificultad_actual | BIGINT | No | Antes de decidir |
| id_item_tipo_ejercicio_actual | BIGINT | No | Antes de decidir |
| puntaje_rendimiento | NUMERIC(8,6) | Sí | Score explicativo |
| fecha_calculo | TIMESTAMPTZ | No | Momento |

## ADP_EVENTO_ADAPTACION
Decisión automática auditable.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_evento_adaptacion | BIGINT | No | PK |
| id_contexto_aprendizaje | BIGINT | No | Contexto |
| id_regla_adaptacion | BIGINT | Sí | Regla; nullable para IA futura |
| id_item_origen_decision | BIGINT | No | REGLAS/IA/HIBRIDO |
| id_item_nivel_rendimiento | BIGINT | No | Resultado |
| id_item_accion_principal | BIGINT | No | Acción |
| id_item_dificultad_anterior | BIGINT | No | Antes |
| id_item_dificultad_nueva | BIGINT | No | Después |
| id_item_tipo_ejercicio_anterior | BIGINT | Sí | Antes |
| id_item_tipo_ejercicio_nuevo | BIGINT | Sí | Después |
| motivo | VARCHAR(1000) | No | Explicación |
| version_motor | VARCHAR(40) | No | Versión lógica |
| fecha_decision | TIMESTAMPTZ | No | Momento |
| aplicacion_exitosa | BOOLEAN | No | Resultado |
| ...auditoría |  |  | Campos comunes |

## ADP_ACCION_EVENTO
Acciones ejecutadas por evento.

| Columna | Tipo | Nulo | Semántica |
|---|---|---:|---|
| id_evento_adaptacion | BIGINT | No | FK/PK |
| secuencia | SMALLINT | No | PK |
| id_item_accion_adaptacion | BIGINT | No | Acción |
| detalle | VARCHAR(500) | Sí | Explicación |
| valor_anterior | VARCHAR(200) | Sí | Snapshot textual |
| valor_nuevo | VARCHAR(200) | Sí | Snapshot textual |
| ejecutada | BOOLEAN | No | Confirmación |
