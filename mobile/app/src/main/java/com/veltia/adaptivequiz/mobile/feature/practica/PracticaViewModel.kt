package com.veltia.adaptivequiz.mobile.feature.practica

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veltia.adaptivequiz.mobile.core.util.mensajeAmigable
import com.veltia.adaptivequiz.mobile.core.util.TiempoRespuesta
import com.veltia.adaptivequiz.mobile.domain.model.ResultadoIntento
import com.veltia.adaptivequiz.mobile.domain.model.SesionPractica
import com.veltia.adaptivequiz.mobile.domain.model.SiguienteExperiencia
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository
import com.veltia.adaptivequiz.mobile.feature.monitor.MonitorStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PracticaUiState(
    val cargando: Boolean = false,
    val sesion: SesionPractica? = null,
    val experiencia: SiguienteExperiencia? = null,
    val opcionesSeleccionadas: Set<Long> = emptySet(),
    val pistaVisible: Boolean = false,
    val usoPista: Boolean = false,
    val resultado: ResultadoIntento? = null,
    val error: String? = null
)

class PracticaViewModel(
    private val repository: AdaptiveQuizRepository,
    private val monitorStore: MonitorStore,
    private val estudianteId: Long,
    private val relojMs: () -> Long = { SystemClock.elapsedRealtime() }
) : ViewModel() {
    private val mutableEstado = MutableStateFlow(PracticaUiState())
    val estado: StateFlow<PracticaUiState> = mutableEstado.asStateFlow()
    private var presentadoEnMs: Long = 0L

    fun iniciar(temaId: Long) = viewModelScope.launch {
        mutableEstado.value = mutableEstado.value.copy(cargando = true, error = null, resultado = null)
        runCatching {
            val sesion = repository.iniciarSesion(estudianteId, temaId)
            sesion to repository.siguienteEjercicio(estudianteId, temaId)
        }.onSuccess { (sesion, experiencia) ->
            presentadoEnMs = relojMs()
            mutableEstado.value = PracticaUiState(sesion = sesion, experiencia = experiencia)
        }.onFailure { error ->
            mutableEstado.value = mutableEstado.value.copy(cargando = false, error = error.mensajeAmigable())
        }
    }

    fun seleccionarOpcion(idOpcion: Long) {
        mutableEstado.value = mutableEstado.value.copy(opcionesSeleccionadas = setOf(idOpcion), error = null)
    }

    fun mostrarPista() {
        val estadoActual = mutableEstado.value
        if (estadoActual.experiencia?.pistaHabilitada == true) {
            mutableEstado.value = estadoActual.copy(pistaVisible = true, usoPista = true)
        }
    }

    fun responder() = viewModelScope.launch {
        val estadoActual = mutableEstado.value
        val sesion = estadoActual.sesion ?: return@launch
        val experiencia = estadoActual.experiencia ?: return@launch
        if (estadoActual.opcionesSeleccionadas.isEmpty()) {
            mutableEstado.value = estadoActual.copy(error = "Selecciona una opción antes de responder.")
            return@launch
        }
        mutableEstado.value = estadoActual.copy(cargando = true, error = null)
        runCatching {
            repository.registrarIntento(
                sesion.id,
                experiencia.ejercicio.id,
                estadoActual.opcionesSeleccionadas.toList(),
                TiempoRespuesta.transcurridoMs(presentadoEnMs, relojMs()),
                estadoActual.usoPista
            )
        }.onSuccess { resultado ->
            monitorStore.publicar(resultado.adaptacion)
            mutableEstado.value = estadoActual.copy(cargando = false, resultado = resultado)
        }.onFailure { error ->
            mutableEstado.value = estadoActual.copy(cargando = false, error = error.mensajeAmigable())
        }
    }

    fun continuar() = viewModelScope.launch {
        val sesion = mutableEstado.value.sesion ?: return@launch
        mutableEstado.value = mutableEstado.value.copy(cargando = true, error = null, resultado = null)
        runCatching { repository.siguienteEjercicio(estudianteId, sesion.temaId) }
            .onSuccess { experiencia ->
                presentadoEnMs = relojMs()
                mutableEstado.value = mutableEstado.value.copy(
                    cargando = false,
                    experiencia = experiencia,
                    opcionesSeleccionadas = emptySet(),
                    pistaVisible = false,
                    usoPista = false
                )
            }.onFailure { error ->
                mutableEstado.value = mutableEstado.value.copy(cargando = false, error = error.mensajeAmigable())
            }
    }
}
