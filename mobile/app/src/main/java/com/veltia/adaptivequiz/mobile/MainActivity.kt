package com.veltia.adaptivequiz.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.veltia.adaptivequiz.mobile.core.network.NetworkModule
import com.veltia.adaptivequiz.mobile.core.ui.AdaptiveQuizTheme
import com.veltia.adaptivequiz.mobile.core.ui.AppViewModelFactory
import com.veltia.adaptivequiz.mobile.feature.inicio.InicioViewModel
import com.veltia.adaptivequiz.mobile.feature.configuracion.ConfiguracionAdaptativaViewModel
import com.veltia.adaptivequiz.mobile.feature.monitor.MonitorStore
import com.veltia.adaptivequiz.mobile.feature.monitor.MonitorViewModel
import com.veltia.adaptivequiz.mobile.feature.practica.PracticaViewModel
import com.veltia.adaptivequiz.mobile.feature.progreso.ProgresoViewModel
import com.veltia.adaptivequiz.mobile.navigation.AdaptiveQuizNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = AppViewModelFactory(NetworkModule.repository(), MonitorStore(), BuildConfig.DEMO_STUDENT_ID)
        setContent {
            AdaptiveQuizTheme {
                val inicio: InicioViewModel = viewModel(factory = factory)
                val practica: PracticaViewModel = viewModel(factory = factory)
                val progreso: ProgresoViewModel = viewModel(factory = factory)
                val monitor: MonitorViewModel = viewModel(factory = factory)
                val configuracion: ConfiguracionAdaptativaViewModel = viewModel(factory = factory)
                Surface {
                    AdaptiveQuizNavHost(inicio, practica, progreso, monitor, configuracion)
                }
            }
        }
    }
}
