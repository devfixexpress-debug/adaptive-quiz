package com.veltia.adaptivequiz.mobile.feature.monitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MonitorViewModel(
    private val repository: AdaptiveQuizRepository,
    private val store: MonitorStore,
    private val estudianteId: Long
) : ViewModel() {
    val ultimaAdaptacion: StateFlow<com.veltia.adaptivequiz.mobile.domain.model.Adaptacion?> = store.ultimaAdaptacion

    fun cargarUltima() = viewModelScope.launch {
        runCatching { repository.adaptaciones(estudianteId).firstOrNull() }
            .getOrNull()
            ?.let(store::publicar)
    }
}
