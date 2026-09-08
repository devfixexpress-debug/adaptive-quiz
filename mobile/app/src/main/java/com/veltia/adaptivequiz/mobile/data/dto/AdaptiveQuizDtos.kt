package com.veltia.adaptivequiz.mobile.data.dto

import java.math.BigDecimal

data class AsignaturaDto(val id: Long, val codigo: String, val nombre: String, val descripcion: String?)
data class TemaDto(
    val id: Long,
    val idAsignatura: Long,
    val codigo: String,
    val nombre: String,
    val descripcion: String?,
    val orden: Int
)

data class OpcionDto(val id: Long, val codigo: String, val texto: String, val orden: Int)
data class PistaDto(val id: Long, val orden: Int, val texto: String, val penalizacionPuntaje: BigDecimal)
data class EjercicioDto(
    val id: Long,
    val idTema: Long,
    val codigo: String,
    val enunciado: String,
    val tipoEjercicio: String,
    val dificultad: String,
    val estado: String,
    val tiempoObjetivoSegundos: Int?,
    val puntajeBase: BigDecimal,
    val opciones: List<OpcionDto>,
    val pistas: List<PistaDto>
)

data class ProgresoTemaDto(
    val idProgresoTema: Long,
    val estudianteId: Long,
    val temaId: Long,
    val dificultadActual: String,
    val totalIntentos: Int,
    val totalAciertos: Int,
    val porcentajeAcierto: BigDecimal,
    val tiempoPromedioMs: Int,
    val rachaAciertosActual: Int,
    val rachaErroresActual: Int,
    val fechaUltimoIntento: String?
)

data class SesionPracticaDto(
    val idSesionPractica: Long,
    val estudianteId: Long,
    val temaId: Long,
    val politicaAdaptacionId: Long,
    val estado: String,
    val fechaInicio: String,
    val cantidadIntentos: Int,
    val progreso: ProgresoTemaDto
)

data class SiguienteEjercicioDto(
    val ejercicio: EjercicioDto,
    val progreso: ProgresoTemaDto,
    val pistaHabilitada: Boolean,
    val numeroPregunta: Int,
    val fallbackPorAgotamiento: Boolean
)

data class ContextoAprendizajeDto(
    val idContextoAprendizaje: Long,
    val intentoDisparadorId: Long,
    val estudianteId: Long,
    val temaId: Long,
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

data class AccionAdaptacionDto(
    val secuencia: Int,
    val accion: String,
    val detalle: String,
    val valorAnterior: String?,
    val valorNuevo: String?,
    val ejecutada: Boolean
)

data class AdaptacionDto(
    val idAdaptacion: Long,
    val rendimiento: String,
    val regla: String?,
    val accionPrincipal: String,
    val dificultadAnterior: String,
    val dificultadNueva: String,
    val tipoEjercicioAnterior: String?,
    val tipoEjercicioNuevo: String?,
    val pistaHabilitada: Boolean,
    val motivo: String,
    val versionMotor: String,
    val fechaDecision: String,
    val aplicacionExitosa: Boolean,
    val contexto: ContextoAprendizajeDto?,
    val acciones: List<AccionAdaptacionDto>
)

data class ResultadoIntentoDto(
    val correcto: Boolean,
    val resultado: String,
    val explicacion: String?,
    val progreso: ProgresoTemaDto,
    val adaptacion: AdaptacionDto
)

data class IniciarSesionRequestDto(val estudianteId: Long, val temaId: Long)
data class RegistrarIntentoRequestDto(
    val sesionPracticaId: Long,
    val ejercicioId: Long,
    val opcionesSeleccionadas: List<Long>,
    val tiempoRespuestaMs: Int,
    val usoPista: Boolean
)
