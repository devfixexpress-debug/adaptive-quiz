package com.veltia.adaptivequiz.mobile.feature.monitor

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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.veltia.adaptivequiz.mobile.core.util.TiempoRespuesta
import com.veltia.adaptivequiz.mobile.domain.model.Adaptacion

@Composable
fun MonitorScreen(viewModel: MonitorViewModel, onVolver: () -> Unit) {
    val adaptacion by viewModel.ultimaAdaptacion.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.cargarUltima() }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver") }
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Outlined.AutoGraph, null)
            Spacer(Modifier.width(8.dp))
            Text("Monitor Adaptativo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Text("CONTEXTO  ↓  PROCESAMIENTO  ↓  DECISIÓN  ↓  ADAPTACIÓN", color = MaterialTheme.colorScheme.primary)
        if (adaptacion == null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Responde un ejercicio para visualizar una decisión adaptativa real.",
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {
            MonitorContenido(adaptacion!!)
        }
    }
}

@Composable
private fun MonitorContenido(adaptacion: Adaptacion) {
    val contexto = adaptacion.contexto
    Etapa("CONTEXTO ACTUAL") {
        if (contexto == null) {
            Text("El contexto estará disponible al consultar el detalle de la adaptación.")
        } else {
            Metrica("Precisión", "${(contexto.porcentajeAcierto.toDouble() * 100).toInt()} %")
            Metrica("Tiempo promedio", TiempoRespuesta.segundos(contexto.tiempoPromedioMs))
            Metrica("Racha", when {
                contexto.rachaAciertos > 0 -> "${contexto.rachaAciertos} aciertos"
                else -> "${contexto.rachaErrores} errores"
            })
            Metrica("Dificultad anterior", adaptacion.dificultadAnterior)
        }
    }
    Flecha()
    Etapa("PROCESAMIENTO") {
        Metrica("Rendimiento", adaptacion.rendimiento)
        contexto?.puntajeRendimiento?.let { Metrica("Puntaje explicativo", it.toPlainString()) }
    }
    Flecha()
    Etapa("DECISIÓN") {
        Metrica("Regla", adaptacion.regla ?: "Fallback configurado")
        Metrica("Acción principal", adaptacion.accionPrincipal)
    }
    Flecha()
    Etapa("ADAPTACIÓN") {
        Text(
            "${adaptacion.dificultadAnterior} → ${adaptacion.dificultadNueva}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        if (adaptacion.pistaHabilitada) Text("Pista habilitada para la siguiente experiencia.")
        Text("Motivo", fontWeight = FontWeight.SemiBold)
        Text(adaptacion.motivo)
    }
}

@Composable
private fun Etapa(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            contenido()
        }
    }
}

@Composable
private fun Metrica(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun Flecha() {
    Text("↓", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.headlineMedium)
}
