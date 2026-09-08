# Backend AdaptiveQuiz

Backend independiente de VAEF, construido con el patrón modular del generador VAEF y el namespace `com.veltia.adaptivequiz`.

## Módulos

- `adaptive-quiz-domain`: modelos, puertos y contratos de adaptación.
- `adaptive-quiz-application`: casos de uso y servicios de aplicación.
- `adaptive-quiz-infrastructure`: JPA, adaptadores de repositorio y mappers.
- `adaptive-quiz-api`: Spring Boot, REST, Flyway, OpenAPI y Actuator.

## Fuente de verdad de datos

Los scripts fuente permanecen exclusivamente en:

- `../database/migrations/postgresql/V1__adaptive_quiz_schema.sql`
- `../database/seeds/postgresql/V1_1__adaptive_quiz_seed.sql`
- `../database/migrations/postgresql/V2__adaptive_quiz_mvp.sql`

El módulo API los empaqueta como `classpath:db/migration` durante Maven. Hibernate valida el esquema; no lo crea.

## Ejecución local

Desde `adaptive-quiz/`:

~~~powershell
Copy-Item .env.example .env
$env:ADAPTIVEQUIZ_ENV_FILE = (Resolve-Path .env).Path
docker compose --env-file .env up -d db
Push-Location backend
mvn clean verify
mvn -pl adaptive-quiz-api -am spring-boot:run
~~~

Use `/actuator/health`, `/api-docs` y `/swagger-ui/index.html` para verificar el servicio. Las credenciales se proveen por variables de entorno o `.env` no versionado; `application.yml` no contiene credenciales por defecto.
