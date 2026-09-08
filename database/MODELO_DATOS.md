# Modelo de Datos — AdaptiveQuiz

## 1. Metamodelo paramétrico

### Catálogos
- `CAT_CATALOGO`
- `CAT_ITEM_CATALOGO`

### Configuración
- `CFG_PARAMETRO`
- `CFG_POLITICA_ADAPTACION`
- `CFG_REGLA_ADAPTACION`

Estos objetos definen escenarios variables sin alterar las entidades transaccionales.

## 2. Parque de datos

### Definición académica
- `ACA_ASIGNATURA`
- `ACA_TEMA`
- `BAN_EJERCICIO`
- `BAN_OPCION_EJERCICIO`
- `BAN_PISTA_EJERCICIO`

### Existencia/estado de aprendizaje
- `APR_ESTUDIANTE`
- `APR_PROGRESO_TEMA`

## 3. Eventos reales
- `PRA_SESION_PRACTICA`
- `PRA_INTENTO`
- `PRA_RESPUESTA`
- `PRA_RESPUESTA_OPCION`

## 4. Adaptación
- `ADP_CONTEXTO_APRENDIZAJE`
- `ADP_EVENTO_ADAPTACION`
- `ADP_ACCION_EVENTO`

## 5. Cadena de eventos
`PRA_INTENTO` es la evidencia primaria. El contexto y el evento adaptativo son snapshots
derivados y auditables.

## 6. Decisiones de robustez
- dificultades, estados, tipos, rendimientos, acciones y orígenes son ítems de catálogo;
- políticas y reglas son versionables;
- los intentos no se sobrescriben;
- el progreso agregado puede recalcularse desde intentos;
- los eventos adaptativos conservan la explicación histórica;
- la IA futura se representa como origen de decisión, no como columna ad-hoc.
