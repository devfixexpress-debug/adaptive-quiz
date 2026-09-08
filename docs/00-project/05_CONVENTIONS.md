# Convenciones

## Idioma
- Pantallas y documentación: español.
- Tablas y columnas: español.
- Código Java/Kotlin: nombres técnicos en inglés permitidos.
- API: recursos en español o inglés; para este taller se normaliza en español.

## Base de datos
- Tablas: MAYÚSCULAS en DDL, `PREFIJO_ENTIDAD`.
- Columnas: `snake_case`, español.
- PK: `id_<entidad>`.
- FK: mismo nombre de la PK referenciada.
- Constraints:
  - `PK_<tabla>`
  - `FK_<tabla>_<referencia>`
  - `UK_<tabla>_<concepto>`
  - `CK_<tabla>_<concepto>`
  - `IX_<tabla>_<columnas>`

## Prefijos funcionales
- `CAT_`: catálogos y clasificaciones.
- `CFG_`: configuración y parámetros.
- `ACA_`: estructura académica.
- `BAN_`: banco de ejercicios.
- `APR_`: aprendizaje y estado/progreso.
- `PRA_`: práctica e intentos.
- `ADP_`: adaptación.
- `AUD_`: auditoría, si se incorpora una bitácora general.

## Git
Conventional Commits y relación obligatoria con Issues.
