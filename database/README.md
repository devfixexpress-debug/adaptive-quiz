# Base de datos

## Motor de referencia
PostgreSQL 17.

## Filosofía
- robustez semántica;
- estabilidad paramétrica;
- separación catálogo/parque;
- eventos explícitos;
- integridad referencial;
- normalización 3FN como base;
- resúmenes derivados sólo cuando tienen propósito explícito.

## Archivos
- `MODELO_DATOS.md`
- `DICCIONARIO_DATOS.md`
- `NORMALIZACION_INTEGRIDAD.md`
- `migrations/postgresql/V1__adaptive_quiz_schema.sql`
- `seeds/postgresql/V1_1__adaptive_quiz_seed.sql`
- `migrations/postgresql/V2__adaptive_quiz_mvp.sql`
- `diagrams/*.mmd`

V1, V1.1 y V2 son migraciones certificadas e inmutables. El módulo `adaptive-quiz-api` las
empaqueta como recursos de Flyway sin duplicar su fuente canónica.
