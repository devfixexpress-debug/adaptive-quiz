package com.veltia.adaptivequiz.mobile.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository
import com.veltia.adaptivequiz.mobile.feature.inicio.InicioViewModel
import com.veltia.adaptivequiz.mobile.feature.monitor.MonitorStore
import com.veltia.adaptivequiz.mobile.feature.monitor.MonitorViewModel
import com.veltia.adaptivequiz.mobile.feature.practica.PracticaViewModel
import com.veltia.adaptivequiz.mobile.feature.progreso.ProgresoViewModel

class AppViewModelFactory(
    private val repository: AdaptiveQuizRepository,
    private val monitorStore: MonitorStore,
    private val estudianteId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(InicioViewModel::class.java) -> InicioViewModel(repository) as T
        modelClass.isAssignableFrom(PracticaViewModel::class.java) ->
            PracticaViewModel(repository, monitorStore, estudianteId) as T
        modelClass.isAssignableFrom(ProgresoViewModel::class.java) -> ProgresoViewModel(repository, estudianteId) as T
        modelClass.isAssignableFrom(MonitorViewModel::class.java) -> MonitorViewModel(repository, monitorStore, estudianteId) as T
        else -> throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
