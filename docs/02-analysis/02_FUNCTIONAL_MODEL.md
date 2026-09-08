# Modelo Funcional

## Misión
Proporcionar práctica académica que se ajuste automáticamente al rendimiento del estudiante.

## Funciones
1. **Definir contenido**
   - administrar asignaturas;
   - administrar temas;
   - definir ejercicios, opciones y pistas.
2. **Practicar**
   - iniciar sesión;
   - seleccionar ejercicio;
   - registrar respuesta;
   - calificar intento.
3. **Adaptar**
   - construir contexto;
   - analizar rendimiento;
   - decidir acción;
   - ejecutar adaptación.
4. **Informar**
   - mostrar progreso;
   - mostrar monitor adaptativo;
   - consultar historial.
5. **Configurar**
   - mantener catálogos;
   - mantener parámetros;
   - versionar políticas y reglas.

## Producto del sistema
Una experiencia de práctica adaptativa.

## Evento elemental de negocio
`INTENTO_RESUELTO`: el estudiante concluye un ejercicio. Este evento alimenta el parque de
datos, actualiza progreso y puede disparar una decisión adaptativa.
