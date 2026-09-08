# Validación de PostgreSQL: esquema y seed

**Motor validado:** PostgreSQL 17.11 (imagen 'postgres:17-alpine').  
**Identificador:** EVD-DB-001  
**Fuente canónica:** 'database/migrations/postgresql/V1__adaptive_quiz_schema.sql' y 'database/seeds/postgresql/V1_1__adaptive_quiz_seed.sql'.

## Resultado de la revisión

No se encontró un error sintáctico, de orden de creación o de integridad que requiriera cambiar el DDL canónico. En particular, 'CFG_REGLA_ADAPTACION' ya declara sus columnas de acción y tipo destino, sus FK, checks de rangos, vigencia y rachas; el seed es compatible con esa definición.

## Ejecución desde una base limpia

Se levantó PostgreSQL local con el Compose del proyecto y se aplicaron los dos scripts canónicos en una base temporal vacía, 'adaptivequiz_validation', con 'psql -v ON_ERROR_STOP=1'. Después se eliminó esa base temporal. Por separado, se recreó explícitamente el schema 'public' de la base activa 'adaptivequiz' y se arrancó el backend para comprobar que Flyway aplicara esas mismas fuentes.

~~~powershell
docker compose --env-file .env.example up -d db
docker cp database/migrations/postgresql/V1__adaptive_quiz_schema.sql adaptive-quiz-db-1:/tmp/V1__adaptive_quiz_schema.sql
docker cp database/seeds/postgresql/V1_1__adaptive_quiz_seed.sql adaptive-quiz-db-1:/tmp/V1_1__adaptive_quiz_seed.sql
docker compose --env-file .env.example exec -T db createdb -U adaptivequiz adaptivequiz_validation
docker compose --env-file .env.example exec -T db psql -U adaptivequiz -d adaptivequiz_validation -v ON_ERROR_STOP=1 -f /tmp/V1__adaptive_quiz_schema.sql
docker compose --env-file .env.example exec -T db psql -U adaptivequiz -d adaptivequiz_validation -v ON_ERROR_STOP=1 -f /tmp/V1_1__adaptive_quiz_seed.sql
docker compose --env-file .env.example exec -T db dropdb -U adaptivequiz adaptivequiz_validation

# Sólo en el contenedor local de validación, antes del arranque por Flyway:
docker compose --env-file .env.example exec -T db psql -U adaptivequiz -d adaptivequiz -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
~~~

El build del módulo API empaqueta los scripts directamente desde los dos directorios canónicos anteriores bajo 'classpath:db/migration'. Flyway registró y aplicó:

| Versión | Descripción | Resultado |
|---|---|---|
| 1 | adaptive quiz schema | correcta |
| 1.1 | adaptive quiz seed | correcta |

## Resultado de estructura

| Verificación | Resultado |
|---|---:|
| Tablas de aplicación | 19 |
| PK | 19 |
| FK | 46 |
| UK | 17 |
| CHECK | 34 |
| Índices 'IX_*' | 32 |
| Ciclos FK no autorreferentes | 0 |
| Referencias huérfanas de 'CAT_ITEM_CATALOGO' | 0 |
| Ejercicios de opción única sin exactamente una respuesta correcta | 0 |

'flyway_schema_history' agrega su propia PK y no se incluye en el conteo de las 19 tablas de aplicación.

## Resultado del seed

| Dato esperado | Resultado |
|---|---:|
| Catálogos | 10 |
| Ítems de catálogo | 31 |
| Política base | 1 |
| Reglas adaptativas | 4 |
| Asignatura Matemática | 1 |
| Tema Álgebra | 1 |
| Estudiante demo | 1 |
| Ejercicios | 15 |
| Opciones | 60 |
| Pistas | 15 |

La política 'POLITICA_BASE_TALLER' quedó en versión 1, ventana 5, con 'R_BAJO_RACHA', 'R_BAJO_PRECISION', 'R_ALTO' y 'R_MEDIO'. Se verificaron cinco ejercicios por cada dificultad: BÁSICO, INTERMEDIO y AVANZADO.

## Incidencia operativa local

En esta estación el puerto publicado 5432 no autenticaba contra la misma ruta verificada dentro de Docker. Se cambió exclusivamente el puerto expuesto de desarrollo a 55432 en '.env.example' y se validó una conexión TCP desde el host. El contenedor, volumen, base y modelo no cambiaron por esta medida.

La evidencia de arranque del backend, Flyway y llamadas HTTP reales se conserva en [BACKEND_RUNTIME_VALIDATION.md](BACKEND_RUNTIME_VALIDATION.md).
