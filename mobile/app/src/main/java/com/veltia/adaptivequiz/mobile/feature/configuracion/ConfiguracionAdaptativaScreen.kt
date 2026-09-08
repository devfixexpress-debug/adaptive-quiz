package com.veltia.adaptivequiz.mobile.feature.configuracion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.veltia.adaptivequiz.mobile.core.util.Carga

@Composable
fun ConfiguracionAdaptativaScreen(
    viewModel: ConfiguracionAdaptativaViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.cargar() }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Volver") }
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Outlined.Settings, null)
            Spacer(Modifier.width(8.dp))
            Text("Configuración adaptativa", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        AssistChip(onClick = {}, label = { Text("Uso docente / demostración") })
        Text(
            "Estos valores controlan cómo el motor interpreta el rendimiento. El estudiante no selecciona manualmente su dificultad.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        estado.mensaje?.let { mensaje ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(mensaje, modifier = Modifier.weight(1f))
                    TextButton(onClick = viewModel::limpiarMensaje) { Text("CERRAR") }
                }
            }
        }

        when (val carga = estado.carga) {
            Carga.Cargando -> CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            Carga.Vacio -> TextoConfiguracion("No existe una política adaptativa activa.")
            is Carga.Error -> ErrorConfiguracion(carga.mensaje, viewModel::cargar)
            is Carga.Exito -> estado.borrador?.let { borrador ->
                FormularioConfiguracion(borrador, estado.guardando, viewModel)
            }
        }
    }

    estado.confirmacion?.let { confirmacion ->
        DialogoConfirmacion(
            confirmacion = confirmacion,
            guardando = estado.guardando,
            onCancelar = viewModel::cancelarConfirmacion,
            onConfirmar = viewModel::confirmar
        )
    }
}

@Composable
private fun FormularioConfiguracion(
    borrador: BorradorConfiguracionAdaptativa,
    guardando: Boolean,
    viewModel: ConfiguracionAdaptativaViewModel
) {
    TarjetaConfiguracion("POLÍTICA") {
        CampoNumero(
            etiqueta = "Ventana de intentos",
            valor = borrador.ventanaIntentos,
            alCambiar = viewModel::actualizarVentana
        )
    }
    TarjetaConfiguracion("RENDIMIENTO ALTO") {
        CampoNumero(
            etiqueta = "Precisión mínima para subir",
            valor = borrador.precisionMinimaAlto,
            sufijo = "%",
            decimal = true,
            alCambiar = viewModel::actualizarPrecisionAlto
        )
        CampoNumero(
            etiqueta = "Tiempo promedio máximo",
            valor = borrador.tiempoMaximoSegundos,
            sufijo = "segundos",
            alCambiar = viewModel::actualizarTiempoAlto
        )
    }
    TarjetaConfiguracion("REFUERZO") {
        CampoNumero(
            etiqueta = "Precisión máxima antes de reforzar",
            valor = borrador.precisionMaximaRefuerzo,
            sufijo = "%",
            decimal = true,
            alCambiar = viewModel::actualizarPrecisionRefuerzo
        )
        CampoNumero(
            etiqueta = "Errores consecutivos",
            valor = borrador.rachaErrores,
            alCambiar = viewModel::actualizarRacha
        )
        InterruptorPista(
            etiqueta = "Activar pista por precisión baja",
            activado = borrador.pistaPorPrecision,
            alCambiar = viewModel::actualizarPistaPrecision
        )
        InterruptorPista(
            etiqueta = "Activar pista por racha de errores",
            activado = borrador.pistaPorRacha,
            alCambiar = viewModel::actualizarPistaRacha
        )
    }
    Button(
        onClick = viewModel::solicitarGuardar,
        modifier = Modifier.fillMaxWidth(),
        enabled = !guardando
    ) {
        Icon(Icons.Outlined.Save, null)
        Spacer(Modifier.width(8.dp))
        Text("GUARDAR CAMBIOS")
    }
    OutlinedButton(
        onClick = viewModel::solicitarRestauracion,
        modifier = Modifier.fillMaxWidth(),
        enabled = !guardando
    ) {
        Icon(Icons.Outlined.Restore, null)
        Spacer(Modifier.width(8.dp))
        Text("RESTAURAR VALORES DEL TALLER")
    }
}

@Composable
private fun TarjetaConfiguracion(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            contenido()
        }
    }
}

@Composable
private fun CampoNumero(
    etiqueta: String,
    valor: String,
    sufijo: String? = null,
    decimal: Boolean = false,
    alCambiar: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        suffix = sufijo?.let { { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = if (decimal) KeyboardType.Decimal else KeyboardType.Number)
    )
}

@Composable
private fun InterruptorPista(etiqueta: String, activado: Boolean, alCambiar: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, modifier = Modifier.weight(1f))
        Switch(checked = activado, onCheckedChange = alCambiar)
    }
}

@Composable
private fun ErrorConfiguracion(mensaje: String, reintentar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("No pudimos cargar la configuración", style = MaterialTheme.typography.titleMedium)
            Text(mensaje)
            OutlinedButton(onClick = reintentar) { Text("REINTENTAR") }
        }
    }
}

@Composable
private fun TextoConfiguracion(texto: String) {
    Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun DialogoConfirmacion(
    confirmacion: ConfirmacionConfiguracion,
    guardando: Boolean,
    onCancelar: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(confirmacion.titulo) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(confirmacion.descripcion)
                confirmacion.cambios.forEach { Text(it) }
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar, enabled = !guardando) { Text("CANCELAR") }
        },
        confirmButton = {
            TextButton(onClick = onConfirmar, enabled = !guardando) { Text("APLICAR") }
        }
    )
}
