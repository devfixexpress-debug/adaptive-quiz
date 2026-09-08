# Componentes certificados

## Android

| Responsabilidad | Componentes reales |
|---|---|
| Navegación y entrada | `MainActivity`, `AdaptiveQuizNavHost`, `InicioScreen`. |
| Práctica | `PracticaScreen`, `PracticaViewModel`, `ResultadoScreen`, `TiempoRespuesta`. |
| Observabilidad adaptativa | `MonitorScreen`, `MonitorViewModel`, `MonitorStore`, `AdaptacionVisual`. |
| Progreso | `ProgresoScreen`, `ProgresoViewModel`. |
| Datos | `AdaptiveQuizApi`, `AdaptiveQuizRepository`, `AdaptiveQuizRepositoryImpl`, `NetworkModule`. |

Compose y los ViewModel no contienen reglas de adaptación: muestran el resultado que retorna la API.

## Backend

| Responsabilidad | Componentes reales |
|---|---|
| REST | `CatalogoAcademicoController`, `EjercicioController`, `PracticaController`, `EstudianteController`, `AdaptacionController`. |
| Casos de uso | `CatalogoAcademicoQueryService`, `EjercicioQueryService`, `PracticaService` y contratos `*UseCase`. |
| Contexto y procesamiento | `LearningContextBuilder`, `PerformanceAnalyzer`, `DefaultPerformanceAnalyzer`. |
| Decisión | `AdaptationEngine`, `AdaptationStrategy`, `RuleBasedAdaptationStrategy`, `AdaptationDecision`. |
| Adaptación y auditoría | `AdaptationActionExecutor`, `AdaptationActionPersistenceService`, `MotorAdaptativoFactory`. |
| Infraestructura | Adaptadores JPA/JDBC, repositorios, mappers, PostgreSQL y Flyway. |

## Persistencia principal

- Catálogos y configuración: `CAT_*`, `CFG_*`.
- Contenido académico: `ACA_*`, `BAN_*`.
- Estudiante y progreso: `APR_*`.
- Práctica: `PRA_*`.
- Adaptación y auditoría: `ADP_*`.

## Estrategias

- `RuleBasedAdaptationStrategy`: estrategia activa y certificada para el Taller 001.
- `AiAdaptationStrategy`: sólo una posibilidad futura detrás de `AdaptationStrategy`; no hay implementación, SDK ni credenciales de IA en v1.0.0.
