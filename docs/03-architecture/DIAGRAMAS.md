# Diagramas de arquitectura

Los diagramas describen componentes certificados y el flujo de configuración para la sustentación. Ninguno representa un selector manual de dificultad del estudiante.

| Diagrama | Propósito |
|---|---|
| [Arquitectura](01_ARCHITECTURE.md) | Módulos Android, backend e infraestructura. |
| [Componentes](02_COMPONENTS.md) | Responsabilidades concretas de UI, API, aplicación, dominio e infraestructura. |
| [Despliegue](03_DEPLOYMENT.md) | Emulador, Spring Boot y PostgreSQL local. |
| [Configuración paramétrica](diagrams/08_CONFIGURACION_PARAMETRICA.mmd) | Docente → API → configuración persistida → motor → decisión automática. |
| [Contexto](../../database/diagrams/01_contexto.mmd) | Datos que regresan de la práctica al backend. |
| [Pipeline adaptativo](../../database/diagrams/02_pipeline_adaptativo.mmd) | Contexto, procesamiento, decisión y adaptación. |
| [Secuencia de adaptación](../../database/diagrams/04_secuencia_adaptacion.mmd) | Interacción entre Android, API, motor y PostgreSQL. |
| [Modelo lógico](../../database/diagrams/06_er_logico.mmd) | Relaciones principales del esquema PostgreSQL. |

## Lectura del diagrama paramétrico

La configuración docente modifica únicamente la ventana de la política activa y los umbrales permitidos de las reglas existentes. `CFG_PARAMETRO` conserva parámetros auxiliares ya consumidos por el analizador. En todos los casos, el motor interpreta el contexto y toma la decisión; no recibe una dificultad elegida por la interfaz.
