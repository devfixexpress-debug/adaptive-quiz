package com.veltia.adaptivequiz.mobile.feature.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veltia.adaptivequiz.mobile.core.util.Carga
import com.veltia.adaptivequiz.mobile.core.util.mensajeAmigable
import com.veltia.adaptivequiz.mobile.domain.model.Asignatura
import com.veltia.adaptivequiz.mobile.domain.model.Tema
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InicioUiState(
    val carga: Carga<List<Asignatura>> = Carga.Cargando,
    val temaDestacado: Tema? = null
)

class InicioViewModel(private val repository: AdaptiveQuizRepository) : ViewModel() {
    private val mutableEstado = MutableStateFlow(InicioUiState())
    val estado: StateFlow<InicioUiState> = mutableEstado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() = viewModelScope.launch {
        mutableEstado.value = mutableEstado.value.copy(carga = Carga.Cargando)
        runCatching {
            val asignaturas = repository.asignaturas()
            val tema = asignaturas.firstOrNull()?.let { repository.temas(it.id).firstOrNull() }
            asignaturas to tema
        }.onSuccess { (asignaturas, tema) ->
            mutableEstado.value = InicioUiState(
                carga = if (asignaturas.isEmpty()) Carga.Vacio else Carga.Exito(asignaturas),
                temaDestacado = tema
            )
        }.onFailure { error ->
            mutableEstado.value = InicioUiState(carga = Carga.Error(error.mensajeAmigable()))
        }
    }
}
