# Docker local

El archivo `../docker-compose.yml` ya define la base de datos de desarrollo de AdaptiveQuiz.
No se construye ni se necesita una imagen de backend para la entrega académica: Spring Boot se
ejecuta localmente con Maven o con el JAR de la versión 1.0.0.

## PostgreSQL

Desde la raíz del repositorio:

```powershell
Copy-Item .env.example .env
docker compose --env-file .env up -d db
docker compose --env-file .env ps
```

La configuración de ejemplo expone PostgreSQL 17 en `localhost:55432` y conserva `5432` dentro
del contenedor. Los datos viven en el volumen `adaptive_quiz_postgres_data`.

No almacene contraseñas reales en el repositorio. Mantenga `.env` sin versionar y use
`.env.example` sólo como plantilla local.
