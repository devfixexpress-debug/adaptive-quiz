package com.veltia.adaptivequiz.mobile.data.remote

import com.veltia.adaptivequiz.mobile.data.dto.AdaptacionDto
import com.veltia.adaptivequiz.mobile.data.dto.ActualizarPoliticaAdaptativaRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.ActualizarReglaAdaptativaRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.AsignaturaDto
import com.veltia.adaptivequiz.mobile.data.dto.ConfiguracionAdaptativaDto
import com.veltia.adaptivequiz.mobile.data.dto.IniciarSesionRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.ProgresoTemaDto
import com.veltia.adaptivequiz.mobile.data.dto.RegistrarIntentoRequestDto
import com.veltia.adaptivequiz.mobile.data.dto.ResultadoIntentoDto
import com.veltia.adaptivequiz.mobile.data.dto.SesionPracticaDto
import com.veltia.adaptivequiz.mobile.data.dto.SiguienteEjercicioDto
import com.veltia.adaptivequiz.mobile.data.dto.TemaDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AdaptiveQuizApi {
    @GET("api/v1/asignaturas")
    suspend fun asignaturas(): List<AsignaturaDto>

    @GET("api/v1/asignaturas/{id}/temas")
    suspend fun temas(@Path("id") idAsignatura: Long): List<TemaDto>

    @POST("api/v1/sesiones-practica")
    suspend fun iniciarSesion(@Body request: IniciarSesionRequestDto): SesionPracticaDto

    @GET("api/v1/ejercicios/siguiente")
    suspend fun siguienteEjercicio(
        @Query("estudianteId") estudianteId: Long,
        @Query("temaId") temaId: Long
    ): SiguienteEjercicioDto

    @POST("api/v1/intentos")
    suspend fun registrarIntento(@Body request: RegistrarIntentoRequestDto): ResultadoIntentoDto

    @GET("api/v1/estudiantes/{id}/progreso")
    suspend fun progreso(@Path("id") idEstudiante: Long): List<ProgresoTemaDto>

    @GET("api/v1/estudiantes/{id}/adaptaciones")
    suspend fun adaptaciones(@Path("id") idEstudiante: Long): List<AdaptacionDto>

    @GET("api/v1/adaptaciones/{id}")
    suspend fun adaptacion(@Path("id") idAdaptacion: Long): AdaptacionDto

    @GET("api/v1/configuracion-adaptativa")
    suspend fun configuracionAdaptativa(): ConfiguracionAdaptativaDto

    @PATCH("api/v1/configuracion-adaptativa/reglas/{codigoRegla}")
    suspend fun actualizarReglaAdaptativa(
        @Path("codigoRegla") codigoRegla: String,
        @Body request: ActualizarReglaAdaptativaRequestDto
    ): ConfiguracionAdaptativaDto

    @PATCH("api/v1/configuracion-adaptativa/politica")
    suspend fun actualizarPoliticaAdaptativa(
        @Body request: ActualizarPoliticaAdaptativaRequestDto
    ): ConfiguracionAdaptativaDto

    @POST("api/v1/configuracion-adaptativa/restaurar-taller")
    suspend fun restaurarConfiguracionTaller(): ConfiguracionAdaptativaDto
}
