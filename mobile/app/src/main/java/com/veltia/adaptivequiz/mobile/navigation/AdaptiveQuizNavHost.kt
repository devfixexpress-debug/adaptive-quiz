package com.veltia.adaptivequiz.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.veltia.adaptivequiz.mobile.feature.inicio.InicioScreen
import com.veltia.adaptivequiz.mobile.feature.inicio.InicioViewModel
import com.veltia.adaptivequiz.mobile.feature.configuracion.ConfiguracionAdaptativaScreen
import com.veltia.adaptivequiz.mobile.feature.configuracion.ConfiguracionAdaptativaViewModel
import com.veltia.adaptivequiz.mobile.feature.monitor.MonitorScreen
import com.veltia.adaptivequiz.mobile.feature.monitor.MonitorViewModel
import com.veltia.adaptivequiz.mobile.feature.practica.PracticaScreen
import com.veltia.adaptivequiz.mobile.feature.practica.PracticaViewModel
import com.veltia.adaptivequiz.mobile.feature.practica.ResultadoScreen
import com.veltia.adaptivequiz.mobile.feature.progreso.ProgresoScreen
import com.veltia.adaptivequiz.mobile.feature.progreso.ProgresoViewModel

private const val INICIO = "inicio"
private const val PRACTICA = "practica"
private const val RESULTADO = "resultado"
private const val MONITOR = "monitor"
private const val PROGRESO = "progreso"
private const val CONFIGURACION_ADAPTATIVA = "configuracion-adaptativa"

@Composable
fun AdaptiveQuizNavHost(
    inicioViewModel: InicioViewModel,
    practicaViewModel: PracticaViewModel,
    progresoViewModel: ProgresoViewModel,
    monitorViewModel: MonitorViewModel,
    configuracionViewModel: ConfiguracionAdaptativaViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = INICIO) {
        composable(INICIO) {
            InicioScreen(
                viewModel = inicioViewModel,
                onComenzar = { temaId ->
                    practicaViewModel.iniciar(temaId)
                    navController.navigate(PRACTICA)
                },
                onMonitor = { navController.navigate(MONITOR) },
                onProgreso = { navController.navigate(PROGRESO) },
                onConfiguracion = { navController.navigate(CONFIGURACION_ADAPTATIVA) }
            )
        }
        composable(PRACTICA) {
            PracticaScreen(
                viewModel = practicaViewModel,
                onResultado = { navController.navigate(RESULTADO) { launchSingleTop = true } },
                onMonitor = { navController.navigate(MONITOR) },
                onProgreso = { navController.navigate(PROGRESO) },
                onSalir = { navController.popBackStack() }
            )
        }
        composable(RESULTADO) {
            ResultadoScreen(
                viewModel = practicaViewModel,
                onSiguiente = {
                    practicaViewModel.continuar()
                    navController.popBackStack()
                },
                onMonitor = { navController.navigate(MONITOR) },
                onInicio = { navController.popBackStack(INICIO, false) }
            )
        }
        composable(MONITOR) {
            MonitorScreen(viewModel = monitorViewModel, onVolver = { navController.popBackStack() })
        }
        composable(PROGRESO) {
            ProgresoScreen(viewModel = progresoViewModel, onVolver = { navController.popBackStack() })
        }
        composable(CONFIGURACION_ADAPTATIVA) {
            ConfiguracionAdaptativaScreen(
                viewModel = configuracionViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
