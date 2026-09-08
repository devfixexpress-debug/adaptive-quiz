package com.veltia.adaptivequiz.mobile.feature.practica

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.veltia.adaptivequiz.mobile.domain.model.Opcion

@Composable
fun PracticaScreen(
    viewModel: PracticaViewModel,
    onResultado: () -> Unit,
    onMonitor: () -> Unit,
    onProgreso: () -> Unit,
    onSalir: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    LaunchedEffect(estado.resultado?.adaptacion?.id) {
        if (estado.resultado != null) onResultado()
    }
    val experiencia = estado.experiencia
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Álgebra", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    experiencia?.let { "Pregunta ${it.numeroPregunta}" } ?: "Preparando práctica",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onProgreso) { Icon(Icons.Outlined.Timeline, "Ver progreso") }
            IconButton(onClick = onMonitor) { Icon(Icons.Outlined.AutoGraph, "Abrir monitor adaptativo") }
            IconButton(onClick = onSalir) { Icon(Icons.Outlined.Close, "Salir de práctica") }
        }
        if (experiencia == null || estado.cargando) {
            Column(Modifier.fillMaxWidth().padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(14.dp))
                Text("Preparando una experiencia personalizada…")
            }
        } else {
            val progresoVisual = (experiencia.numeroPregunta.coerceAtMost(5) / 5f)
            LinearProgressIndicator(progress = { progresoVisual }, modifier = Modifier.fillMaxWidth())
            AssistChip(
                onClick = {},
                label = { Text("Dificultad: ${experiencia.progreso.dificultadActual}") }
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    experiencia.ejercicio.enunciado,
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                experiencia.ejercicio.opciones.forEach { opcion ->
                    OpcionSeleccionable(
                        opcion = opcion,
                        seleccionada = opcion.id in estado.opcionesSeleccionadas,
                        onClick = { viewModel.seleccionarOpcion(opcion.id) }
                    )
                }
            }
            if (experiencia.pistaHabilitada) {
                if (estado.pistaVisible) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Lightbulb, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Pista activada por la adaptación", fontWeight = FontWeight.SemiBold)
                            }
                            Text(experiencia.ejercicio.pistas.firstOrNull()?.texto ?: "Revisa cuidadosamente el enunciado.")
                        }
                    }
                } else {
                    OutlinedButton(onClick = viewModel::mostrarPista, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.Lightbulb, null)
                        Spacer(Modifier.width(8.dp))
                        Text("MOSTRAR PISTA")
                    }
                }
            }
            estado.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = viewModel::responder,
                enabled = estado.opcionesSeleccionadas.isNotEmpty() && !estado.cargando,
                modifier = Modifier.fillMaxWidth()
            ) { Text("RESPONDER") }
        }
    }
}

@Composable
private fun OpcionSeleccionable(opcion: Opcion, seleccionada: Boolean, onClick: () -> Unit) {
    val color = if (seleccionada) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = color,
        tonalElevation = if (seleccionada) 2.dp else 0.dp
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(opcion.codigo, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Text(opcion.texto, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
