package com.veltia.adaptivequiz.mobile.feature.progreso

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.veltia.adaptivequiz.mobile.core.util.Carga
import com.veltia.adaptivequiz.mobile.feature.inicio.EstadoError
import com.veltia.adaptivequiz.mobile.feature.inicio.EstadoVacio
import com.veltia.adaptivequiz.mobile.feature.inicio.TextoCarga

@Composable
fun ProgresoScreen(viewModel: ProgresoViewModel, onVolver: () -> Unit) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.cargar() }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) { Icon(Icons.Outlined.ArrowBack, "Volver") }
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Outlined.Timeline, null)
            Spacer(Modifier.width(8.dp))
            Text("Progreso", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        when (val carga = estado.progreso) {
            Carga.Cargando -> TextoCarga("Consultando tu progreso real…")
            Carga.Vacio -> EstadoVacio("Aún no hay intentos registrados.")
            is Carga.Error -> EstadoError(carga.mensaje, viewModel::cargar)
            is Carga.Exito -> carga.datos.forEach { progreso ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Álgebra", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        DatoProgreso("Intentos", progreso.totalIntentos.toString())
                        DatoProgreso("Precisión", "${(progreso.porcentajeAcierto.toDouble() * 100).toInt()} %")
                        DatoProgreso("Dificultad actual", progreso.dificultadActual)
                        DatoProgreso("Racha", if (progreso.rachaAciertos > 0) "${progreso.rachaAciertos} aciertos" else "${progreso.rachaErrores} errores")
                    }
                }
            }
        }
        if (estado.adaptaciones.isNotEmpty()) {
            Text("Historial reciente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            estado.adaptaciones.take(5).forEach { adaptacion ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("${adaptacion.dificultadAnterior} → ${adaptacion.dificultadNueva}", fontWeight = FontWeight.SemiBold)
                        Text("${adaptacion.regla ?: "Regla configurada"} · ${adaptacion.accionPrincipal}")
                    }
                }
            }
        }
    }
}

@Composable
private fun DatoProgreso(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, fontWeight = FontWeight.SemiBold)
    }
}
