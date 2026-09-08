package com.veltia.adaptivequiz.mobile.feature.monitor

import com.veltia.adaptivequiz.mobile.domain.model.Adaptacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MonitorStore {
    private val mutableUltimaAdaptacion = MutableStateFlow<Adaptacion?>(null)
    val ultimaAdaptacion: StateFlow<Adaptacion?> = mutableUltimaAdaptacion.asStateFlow()

    fun publicar(adaptacion: Adaptacion) {
        mutableUltimaAdaptacion.value = adaptacion
    }
}
