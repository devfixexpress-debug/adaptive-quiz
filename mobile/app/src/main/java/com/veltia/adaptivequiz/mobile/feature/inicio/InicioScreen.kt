package com.veltia.adaptivequiz.mobile.feature.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
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
import com.veltia.adaptivequiz.mobile.core.util.Carga

@Composable
fun InicioScreen(
    viewModel: InicioViewModel,
    onComenzar: (Long) -> Unit,
    onMonitor: () -> Unit,
    onProgreso: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(28.dp))
        Text("AdaptiveQuiz", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
        Text("Aprende a tu ritmo", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text("Estudiante Demo", style = MaterialTheme.typography.bodyLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onProgreso) {
                Icon(Icons.Outlined.Timeline, null)
                Spacer(Modifier.width(6.dp))
                Text("Progreso")
            }
            OutlinedButton(onClick = onMonitor) {
                Icon(Icons.Outlined.AutoGraph, null)
                Spacer(Modifier.width(6.dp))
                Text("Monitor")
            }
        }
        when (val carga = estado.carga) {
            Carga.Cargando -> TextoCarga("Cargando tu catálogo académico…")
            Carga.Vacio -> EstadoVacio("Aún no hay asignaturas disponibles.")
            is Carga.Error -> EstadoError(carga.mensaje, viewModel::cargar)
            is Carga.Exito -> {
                carga.datos.forEach { asignatura ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.School, null)
                                Spacer(Modifier.width(10.dp))
                                Text(asignatura.nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                            }
                            estado.temaDestacado?.let { tema ->
                                Text("Tema · ${tema.nombre}", style = MaterialTheme.typography.titleMedium)
                                Text(tema.descripcion ?: "Práctica adaptativa disponible.")
                                Button(
                                    onClick = { onComenzar(tema.id) },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(vertical = 14.dp)
                                ) {
                                    Icon(Icons.Outlined.PlayArrow, null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("COMENZAR PRÁCTICA")
                                }
                            } ?: Text("No hay temas publicados para esta asignatura.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextoCarga(texto: String) {
    Text(texto, modifier = Modifier.padding(top = 28.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
fun EstadoVacio(texto: String) {
    Text(texto, modifier = Modifier.padding(top = 28.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
fun EstadoError(texto: String, reintentar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("No pudimos cargar esta pantalla", style = MaterialTheme.typography.titleMedium)
            Text(texto)
            ElevatedButton(onClick = reintentar) { Text("REINTENTAR") }
        }
    }
}
