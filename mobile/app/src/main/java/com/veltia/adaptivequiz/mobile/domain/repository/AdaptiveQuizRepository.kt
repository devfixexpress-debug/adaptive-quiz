package com.veltia.adaptivequiz.mobile.domain.repository

import com.veltia.adaptivequiz.mobile.domain.model.Adaptacion
import com.veltia.adaptivequiz.mobile.domain.model.Asignatura
import com.veltia.adaptivequiz.mobile.domain.model.ProgresoTema
import com.veltia.adaptivequiz.mobile.domain.model.ResultadoIntento
import com.veltia.adaptivequiz.mobile.domain.model.SesionPractica
import com.veltia.adaptivequiz.mobile.domain.model.SiguienteExperiencia
import com.veltia.adaptivequiz.mobile.domain.model.Tema

interface AdaptiveQuizRepository {
    suspend fun asignaturas(): List<Asignatura>
    suspend fun temas(idAsignatura: Long): List<Tema>
    suspend fun iniciarSesion(idEstudiante: Long, idTema: Long): SesionPractica
    suspend fun siguienteEjercicio(idEstudiante: Long, idTema: Long): SiguienteExperiencia
    suspend fun registrarIntento(
        idSesion: Long,
        idEjercicio: Long,
        opciones: List<Long>,
        tiempoRespuestaMs: Int,
        usoPista: Boolean
    ): ResultadoIntento
    suspend fun progreso(idEstudiante: Long): List<ProgresoTema>
    suspend fun adaptaciones(idEstudiante: Long): List<Adaptacion>
}
