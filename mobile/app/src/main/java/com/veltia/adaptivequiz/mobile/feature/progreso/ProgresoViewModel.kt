package com.veltia.adaptivequiz.mobile.feature.progreso

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veltia.adaptivequiz.mobile.core.util.Carga
import com.veltia.adaptivequiz.mobile.core.util.mensajeAmigable
import com.veltia.adaptivequiz.mobile.domain.model.Adaptacion
import com.veltia.adaptivequiz.mobile.domain.model.ProgresoTema
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProgresoUiState(
    val progreso: Carga<List<ProgresoTema>> = Carga.Cargando,
    val adaptaciones: List<Adaptacion> = emptyList()
)

class ProgresoViewModel(
    private val repository: AdaptiveQuizRepository,
    private val estudianteId: Long
) : ViewModel() {
    private val mutableEstado = MutableStateFlow(ProgresoUiState())
    val estado: StateFlow<ProgresoUiState> = mutableEstado.asStateFlow()

    fun cargar() = viewModelScope.launch {
        mutableEstado.value = mutableEstado.value.copy(progreso = Carga.Cargando)
        runCatching { repository.progreso(estudianteId) to repository.adaptaciones(estudianteId) }
            .onSuccess { (progresos, adaptaciones) ->
                mutableEstado.value = ProgresoUiState(
                    if (progresos.isEmpty()) Carga.Vacio else Carga.Exito(progresos), adaptaciones
                )
            }.onFailure { error ->
                mutableEstado.value = ProgresoUiState(Carga.Error(error.mensajeAmigable()))
            }
    }
}
