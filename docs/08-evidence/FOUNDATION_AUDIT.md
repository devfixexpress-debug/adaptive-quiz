# Auditoría de foundation y reutilización VAEF

**Corte:** Taller 001, M0/M1 — foundation, base de datos y backend bootstrap.  
**Alcance:** AdaptiveQuiz es independiente de VAEF; esta auditoría registra solamente reutilización de herramientas y patrones técnicos.

## Generador localizado

El generador vigente del workspace se localizó en:

'C:\workspace-java\enterprise-platform\tools\generators\service-generator'

Se revisaron su README, arquitectura, configuración, plantillas y código de generación. Genera un reactor Maven de cuatro módulos, capas de dominio, aplicación, infraestructura y API a partir de metadatos de 'INFORMATION_SCHEMA'.

No se lo ejecutó directamente para AdaptiveQuiz porque su lector de esquema y sus tipos están implementados específicamente para MySQL mediante 'pymysql'. La fuente contractual de AdaptiveQuiz es PostgreSQL; forzar aquel generador habría introducido tipos, consultas o metadatos incorrectos. Se reutilizó de manera trazable como plantilla de estructura y convenciones, sin modificarlo ni crear una dependencia en tiempo de ejecución.

## Servicios de referencia

- 'enterprise-platform/services/learning-service': reactor Maven, separación modular, configuración Spring Boot y convenciones de servicios.
- 'enterprise-platform/services/lavanderia-service': controladores, DTO, manejo uniforme de errores, OpenAPI y configuración operativa.

No se copió ningún modelo, tabla, regla de negocio, package ni dependencia funcional de dichos servicios.

## Patrones reutilizados

- Reactor Maven con cuatro responsabilidades: domain, application, infrastructure y API.
- Puertos de repositorio en dominio y adaptadores JPA en infraestructura.
- Mappers de persistencia y de API separados.
- Casos de uso/servicios de aplicación transaccionales; controladores REST sin SQL.
- Spring Boot, Bean Validation, error uniforme, Actuator y springdoc/OpenAPI.
- Flyway como mecanismo de inicialización y 'spring.jpa.hibernate.ddl-auto=validate'.
- Java 17, Spring Boot 3.3.5, MapStruct y pruebas JUnit de unidades de dominio/aplicación.

## Patrones descartados deliberadamente

- Lector/generador MySQL, 'pymysql', tipos y consultas de MySQL.
- Paquetes 'com.enterprise.learning' y 'com.enterprise.lavanderia'.
- Seguridad, tenancy, entidades, catálogos, reglas y esquemas funcionales propios de VAEF.
- Controladores CRUD genéricos cuando el contrato requiere un caso de uso explícito.
- Cualquier SDK, clave, proveedor o llamada de IA.

## Decisiones tomadas

1. Se usa el namespace propio 'com.veltia.adaptivequiz'.
2. La migración canónica permanece en 'database/'; el módulo API la incorpora al artefacto en tiempo de build, sin una segunda copia fuente.
3. El backend valida el esquema creado por Flyway; Hibernate no genera DDL.
4. El primer slice implementa sólo las consultas de catálogo académico y detalle de ejercicio.
5. La preparación adaptativa se limita a contratos y al esqueleto 'RuleBasedAdaptationStrategy'; no hay comportamiento adaptativo completo ni IA.

La decisión de adaptar el patrón del generador a PostgreSQL se formaliza en [ADR-0006](../03-architecture/adr/ADR-0006-bootstrap-postgresql-independiente.md).

## Hallazgos y tratamiento

| Hallazgo | Tratamiento |
|---|---|
| El modelo de dominio nombra 'AccionAdaptacion', mientras el DDL y el alcance de esta ejecución usan 'ADP_ACCION_EVENTO' / 'AccionEvento'. | Se adoptó 'AccionEvento' en el código para conservar la semántica y nomenclatura de la fuente contractual de datos. No cambia el concepto de dominio. |
| El roadmap histórico ubica el backend funcional en otro hito, pero esta ejecución solicita explícitamente M0/M1 con backend bootstrap. | Se implementó únicamente el alcance solicitado y se registra como [Unreleased]; VERSION se conserva en 0.1.0 hasta que exista una decisión de release SemVer. |
