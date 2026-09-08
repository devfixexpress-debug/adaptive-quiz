package com.veltia.adaptivequiz.mobile.feature.practica

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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.veltia.adaptivequiz.mobile.core.util.toVisual

@Composable
fun ResultadoScreen(
    viewModel: PracticaViewModel,
    onSiguiente: () -> Unit,
    onMonitor: () -> Unit,
    onInicio: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val resultado = estado.resultado
    if (resultado == null) {
        Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
            Text("No hay un resultado activo.")
            OutlinedButton(onClick = onInicio) { Text("VOLVER AL INICIO") }
        }
        return
    }
    val visual = resultado.adaptacion.toVisual()
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (resultado.correcto) Icons.Outlined.CheckCircle else Icons.Outlined.ErrorOutline,
                null,
                tint = if (resultado.correcto) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.width(12.dp))
            Text(
                if (resultado.correcto) "Correcto" else "Incorrecto",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Explicación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(resultado.explicacion ?: "La respuesta fue registrada correctamente.")
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (visual.esRefuerzo) MaterialTheme.colorScheme.tertiaryContainer
                else MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(visual.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(visual.mensaje)
                Text(
                    "${resultado.adaptacion.dificultadAnterior} → ${resultado.adaptacion.dificultadNueva}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(Modifier.weight(1f))
        OutlinedButton(onClick = onMonitor, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Outlined.AutoGraph, null)
            Spacer(Modifier.width(8.dp))
            Text("VER MONITOR ADAPTATIVO")
        }
        Button(onClick = onSiguiente, modifier = Modifier.fillMaxWidth()) { Text("SIGUIENTE EJERCICIO") }
    }
}
