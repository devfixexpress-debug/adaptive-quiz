package com.veltia.adaptivequiz.mobile.domain.model

import java.math.BigDecimal

data class Asignatura(val id: Long, val codigo: String, val nombre: String, val descripcion: String?)
data class Tema(val id: Long, val idAsignatura: Long, val codigo: String, val nombre: String, val descripcion: String?, val orden: Int)
data class Opcion(val id: Long, val codigo: String, val texto: String, val orden: Int)
data class Pista(val id: Long, val orden: Int, val texto: String, val penalizacionPuntaje: BigDecimal)
data class Ejercicio(
    val id: Long,
    val temaId: Long,
    val codigo: String,
    val enunciado: String,
    val tipo: String,
    val dificultad: String,
    val opciones: List<Opcion>,
    val pistas: List<Pista>
)
data class ProgresoTema(
    val id: Long,
    val estudianteId: Long,
    val temaId: Long,
    val dificultadActual: String,
    val totalIntentos: Int,
    val totalAciertos: Int,
    val porcentajeAcierto: BigDecimal,
    val tiempoPromedioMs: Int,
    val rachaAciertos: Int,
    val rachaErrores: Int
)
data class SesionPractica(
    val id: Long,
    val estudianteId: Long,
    val temaId: Long,
    val estado: String,
    val cantidadIntentos: Int,
    val progreso: ProgresoTema
)
data class SiguienteExperiencia(
    val ejercicio: Ejercicio,
    val progreso: ProgresoTema,
    val pistaHabilitada: Boolean,
    val numeroPregunta: Int,
    val fallbackPorAgotamiento: Boolean
)
data class ContextoAprendizaje(
    val id: Long,
    val numeroIntentosVentana: Int,
    val totalAciertosVentana: Int,
    val porcentajeAcierto: BigDecimal,
    val tiempoPromedioMs: Int,
    val rachaAciertos: Int,
    val rachaErrores: Int,
    val dificultadActual: String,
    val tipoEjercicioActual: String,
    val puntajeRendimiento: BigDecimal?
)
data class AccionAdaptacion(val accion: String, val detalle: String, val ejecutada: Boolean)
data class Adaptacion(
    val id: Long,
    val rendimiento: String,
    val regla: String?,
    val accionPrincipal: String,
    val dificultadAnterior: String,
    val dificultadNueva: String,
    val pistaHabilitada: Boolean,
    val motivo: String,
    val contexto: ContextoAprendizaje?,
    val acciones: List<AccionAdaptacion>
)
data class ResultadoIntento(
    val correcto: Boolean,
    val resultado: String,
    val explicacion: String?,
    val progreso: ProgresoTema,
    val adaptacion: Adaptacion
)
