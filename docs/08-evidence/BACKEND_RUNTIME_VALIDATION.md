# Evidencia de ejecución real del backend

**Fecha de ejecución:** 2026-09-08  
**Identificador:** EVD-RUNTIME-001  
**Ambiente:** Spring Boot local, Java 17.0.17, PostgreSQL 17.11 real en Docker, puerto de API 8080.

No se usaron mocks ni Testcontainers como evidencia de esta validación. No se proporcionó un ambiente externo de producción; por ello esta evidencia corresponde a una ejecución real local con el servicio Spring Boot y PostgreSQL, no a un despliegue externo.

## Arranque

1. Se confirmó que 'public' no tenía tablas.
2. Se arrancó el artefacto 'adaptive-quiz-api-0.1.0-SNAPSHOT.jar' con variables de entorno de desarrollo no versionadas.
3. Flyway aplicó V1 y V1.1 en 0.438 s.
4. Hibernate validó el esquema con 'ddl-auto=validate'.
5. Spring Boot inició en 13.662 s.

## Resultado HTTP

| Recurso | Resultado real |
|---|---|
| 'GET /actuator/health' | 200, 'UP'; componentes DB, disco y ping en UP |
| 'GET /api-docs' | 200 |
| 'GET /swagger-ui/index.html' | 200 |
| 'GET /api/v1/asignaturas' | 200; 1 asignatura: 'MAT' / Matemática |
| 'GET /api/v1/asignaturas/1/temas' | 200; 1 tema: 'ALG' / Álgebra |
| 'GET /api/v1/ejercicios/1' | 200; 'ALG-B-001', 4 opciones y 1 pista |
| 'GET /api/v1/ejercicios/99999' | 404; error uniforme 'RECURSO_NO_ENCONTRADO' |
| 'GET /api/v1/asignaturas/0/temas' | 400; error uniforme 'VALIDACION' |

El detalle público del ejercicio no incluye la bandera de respuesta correcta ni la explicación. Esa información se reserva para el futuro caso de uso de calificación.

## Trazas de ejecución observadas

~~~text
Migrating schema "public" to version "1 - adaptive quiz schema"
Migrating schema "public" to version "1.1 - adaptive quiz seed"
Successfully applied 2 migrations to schema "public", now at version v1.1
Started AdaptiveQuizApplication
~~~

Los logs generados localmente no se versionan; los comandos y resultados verificables quedan resumidos en esta evidencia.
