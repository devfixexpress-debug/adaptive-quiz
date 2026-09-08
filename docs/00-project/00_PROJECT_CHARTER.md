# Carta del Proyecto — AdaptiveQuiz

**Versión:** 0.1.0  
**Estado:** Aprobado para implementación

## Problema
Las plataformas de práctica tradicionales suelen presentar secuencias similares a estudiantes
con rendimientos diferentes. Esto puede producir ejercicios demasiado simples para algunos o
demasiado complejos para otros.

## Propuesta
Aplicación móvil que observa automáticamente el desempeño reciente, construye un contexto
de aprendizaje y adapta la siguiente experiencia sin intervención manual.

## Objetivo general
Desarrollar una aplicación móvil capaz de modificar automáticamente su comportamiento en
respuesta al contexto de aprendizaje detectado.

## Objetivos específicos
1. Registrar aciertos, errores y tiempos de respuesta.
2. Construir un contexto a partir de una ventana de intentos recientes.
3. Determinar rendimiento bajo, medio o alto.
4. Aumentar, mantener o disminuir dificultad automáticamente.
5. Permitir adaptar el tipo de ejercicio y la disponibilidad de pistas.
6. Mantener separadas captura, procesamiento, decisión e interfaz.
7. Persistir evidencia de las decisiones adaptativas.
8. Publicar código, documentación y trazabilidad en Git.

## Producto
- App Android ejecutable.
- Backend REST.
- PostgreSQL + Flyway.
- Motor adaptativo determinista.
- Repositorio Git con README, commits, pruebas y documentación.
- IA como extensión posterior.

## Criterio de éxito del Taller
La aplicación debe demostrar en tiempo real que un cambio en el rendimiento produce una
decisión automática y una adaptación observable, sin seleccionar manualmente un nivel.
