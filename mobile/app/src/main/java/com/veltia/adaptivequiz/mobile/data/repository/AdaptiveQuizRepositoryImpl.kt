package com.veltia.adaptivequiz.mobile.data.repository

import com.veltia.adaptivequiz.mobile.data.dto.AccionAdaptacionDto
import com.veltia.adaptivequiz.mobile.data.dto.AdaptacionDto
import com.veltia.adaptivequiz.mobile.data.dto.ActualizarPoliticaAdaptativaRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.ActualizarReglaAdaptativaRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.AsignaturaDto
import com.veltia.adaptivequiz.mobile.data.dto.ConfiguracionAdaptativaDto
import com.veltia.adaptivequiz.mobile.data.dto.ContextoAprendizajeDto
import com.veltia.adaptivequiz.mobile.data.dto.EjercicioDto
import com.veltia.adaptivequiz.mobile.data.dto.IniciarSesionRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.ProgresoTemaDto
import com.veltia.adaptivequiz.mobile.data.dto.RegistrarIntentoRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.ResultadoIntentoDto
import com.veltia.adaptivequiz.mobile.data.dto.SesionPracticaDto
import com.veltia.adaptivequiz.mobile.data.dto.SiguienteEjercicioDto
import com.veltia.adaptivequiz.mobile.data.dto.TemaDto
import com.veltia.adaptivequiz.mobile.data.remote.AdaptiveQuizApi
import com.veltia.adaptivequiz.mobile.domain.model.AccionAdaptacion
import com.veltia.adaptivequiz.mobile.domain.model.Adaptacion
import com.veltia.adaptivequiz.mobile.domain.model.ActualizacionReglaAdaptativa
import com.veltia.adaptivequiz.mobile.domain.model.Asignatura
import com.veltia.adaptivequiz.mobile.domain.model.ConfiguracionAdaptativa
import com.veltia.adaptivequiz.mobile.domain.model.ContextoAprendizaje
import com.veltia.adaptivequiz.mobile.domain.model.Ejercicio
import com.veltia.adaptivequiz.mobile.domain.model.Opcion
import com.veltia.adaptivequiz.mobile.domain.model.Pista
import com.veltia.adaptivequiz.mobile.domain.model.ProgresoTema
import com.veltia.adaptivequiz.mobile.domain.model.PoliticaAdaptativa
import com.veltia.adaptivequiz.mobile.domain.model.ReglaAdaptativa
import com.veltia.adaptivequiz.mobile.domain.model.ResultadoIntento
import com.veltia.adaptivequiz.mobile.domain.model.SesionPractica
import com.veltia.adaptivequiz.mobile.domain.model.SiguienteExperiencia
import com.veltia.adaptivequiz.mobile.domain.model.Tema
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository

class AdaptiveQuizRepositoryImpl(private val api: AdaptiveQuizApi) : AdaptiveQuizRepository {
    override suspend fun asignaturas(): List<Asignatura> = api.asignaturas().map(AsignaturaDto::toDomain)
    override suspend fun temas(idAsignatura: Long): List<Tema> = api.temas(idAsignatura).map(TemaDto::toDomain)
    override suspend fun iniciarSesion(idEstudiante: Long, idTema: Long): SesionPractica =
        api.iniciarSesion(IniciarSesionRequestDto(idEstudiante, idTema)).toDomain()

    override suspend fun siguienteEjercicio(idEstudiante: Long, idTema: Long): SiguienteExperiencia =
        api.siguienteEjercicio(idEstudiante, idTema).toDomain()

    override suspend fun registrarIntento(
        idSesion: Long,
        idEjercicio: Long,
        opciones: List<Long>,
        tiempoRespuestaMs: Int,
        usoPista: Boolean
    ): ResultadoIntento = api.registrarIntento(
        RegistrarIntentoRequestDto(idSesion, idEjercicio, opciones, tiempoRespuestaMs, usoPista)
    ).toDomain()

    override suspend fun progreso(idEstudiante: Long): List<ProgresoTema> = api.progreso(idEstudiante).map(ProgresoTemaDto::toDomain)
    override suspend fun adaptaciones(idEstudiante: Long): List<Adaptacion> = api.adaptaciones(idEstudiante).map(AdaptacionDto::toDomain)

    override suspend fun configuracionAdaptativa(): ConfiguracionAdaptativa = api.configuracionAdaptativa().toDomain()

    override suspend fun actualizarReglaAdaptativa(
        codigoRegla: String,
        cambio: ActualizacionReglaAdaptativa
    ): ConfiguracionAdaptativa = api.actualizarReglaAdaptativa(
        codigoRegla,
        ActualizarReglaAdaptativaRequestDto(
            cambio.porcentajeAciertoMin,
            cambio.porcentajeAciertoMax,
            cambio.tiempoPromedioMaxMs,
            cambio.rachaErroresMin,
            cambio.habilitarPista
        )
    ).toDomain()

    override suspend fun actualizarPoliticaAdaptativa(tamanoVentanaIntentos: Int): ConfiguracionAdaptativa =
        api.actualizarPoliticaAdaptativa(ActualizarPoliticaAdaptativaRequestDto(tamanoVentanaIntentos)).toDomain()

    override suspend fun restaurarConfiguracionTaller(): ConfiguracionAdaptativa =
        api.restaurarConfiguracionTaller().toDomain()
}

fun AsignaturaDto.toDomain() = Asignatura(id, codigo, nombre, descripcion)
fun TemaDto.toDomain() = Tema(id, idAsignatura, codigo, nombre, descripcion, orden)
fun EjercicioDto.toDomain() = Ejercicio(
    id, idTema, codigo, enunciado, tipoEjercicio, dificultad,
    opciones.map { Opcion(it.id, it.codigo, it.texto, it.orden) },
    pistas.map { Pista(it.id, it.orden, it.texto, it.penalizacionPuntaje) }
)
fun ProgresoTemaDto.toDomain() = ProgresoTema(
    idProgresoTema, estudianteId, temaId, dificultadActual, totalIntentos, totalAciertos,
    porcentajeAcierto, tiempoPromedioMs, rachaAciertosActual, rachaErroresActual
)
fun SesionPracticaDto.toDomain() = SesionPractica(
    idSesionPractica, estudianteId, temaId, estado, cantidadIntentos, progreso.toDomain()
)
fun SiguienteEjercicioDto.toDomain() = SiguienteExperiencia(
    ejercicio.toDomain(), progreso.toDomain(), pistaHabilitada, numeroPregunta, fallbackPorAgotamiento
)
fun ContextoAprendizajeDto.toDomain() = ContextoAprendizaje(
    idContextoAprendizaje, numeroIntentosVentana, totalAciertosVentana, porcentajeAcierto,
    tiempoPromedioMs, rachaAciertos, rachaErrores, dificultadActual, tipoEjercicioActual, puntajeRendimiento
)
fun AccionAdaptacionDto.toDomain() = AccionAdaptacion(accion, detalle, ejecutada)
fun AdaptacionDto.toDomain() = Adaptacion(
    idAdaptacion, rendimiento, regla, accionPrincipal, dificultadAnterior, dificultadNueva,
    pistaHabilitada, motivo, contexto?.toDomain(), acciones.map(AccionAdaptacionDto::toDomain)
)
fun ResultadoIntentoDto.toDomain() = ResultadoIntento(
    correcto, resultado, explicacion, progreso.toDomain(), adaptacion.toDomain()
)
fun ConfiguracionAdaptativaDto.toDomain() = ConfiguracionAdaptativa(
    PoliticaAdaptativa(
        politica.codigo,
        politica.nombre,
        politica.version,
        politica.tamanoVentanaIntentos
    ),
    reglas.map {
        ReglaAdaptativa(
            it.codigo,
            it.nombre,
            it.prioridad,
            it.porcentajeAciertoMin,
            it.porcentajeAciertoMax,
            it.tiempoPromedioMaxMs,
            it.rachaErroresMin,
            it.habilitarPista
        )
    }
)
