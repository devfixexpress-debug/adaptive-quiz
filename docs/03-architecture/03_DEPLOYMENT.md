# Despliegue local reproducible

## Topología certificada

```text
Emulador Android
http://10.0.2.2:8080
          │ HTTP REST
          ▼
Spring Boot local :8080
          │ JDBC / JPA + Flyway
          ▼
PostgreSQL Docker
host :55432 → contenedor :5432
```

El teléfono físico fue reconocido durante la certificación, pero el smoke se ejecutó en `emulator-5554`. Para un teléfono físico se cambia únicamente `adaptiveQuizApiBaseUrl` por la dirección de red accesible del backend.

## Inicio

1. Copiar `.env.example` a `.env` y arrancar `docker compose --env-file .env up -d db`.
2. Definir `ADAPTIVEQUIZ_ENV_FILE` y arrancar el JAR de Spring Boot 1.0.0.
3. Validar `/actuator/health`, `/api-docs` y `/swagger-ui/index.html`.
4. Abrir `mobile/` en Android Studio y ejecutar la configuración `app` en el emulador.

Las instrucciones completas están en el [README](../../README.md). La demostración es local y real; no se afirma ni se requiere despliegue cloud, TLS o un proveedor de IA.
