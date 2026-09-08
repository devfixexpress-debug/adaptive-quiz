package com.veltia.adaptivequiz.mobile.feature.configuracion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veltia.adaptivequiz.mobile.core.util.Carga
import com.veltia.adaptivequiz.mobile.core.util.mensajeAmigable
import com.veltia.adaptivequiz.mobile.domain.model.ActualizacionReglaAdaptativa
import com.veltia.adaptivequiz.mobile.domain.model.ConfiguracionAdaptativa
import com.veltia.adaptivequiz.mobile.domain.model.ReglaAdaptativa
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository
import java.math.BigDecimal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val REGLA_ALTO = "R_ALTO"
private const val REGLA_BAJO_PRECISION = "R_BAJO_PRECISION"
private const val REGLA_BAJO_RACHA = "R_BAJO_RACHA"

data class BorradorConfiguracionAdaptativa(
    val ventanaIntentos: String,
    val precisionMinimaAlto: String,
    val tiempoMaximoSegundos: String,
    val precisionMaximaRefuerzo: String,
    val rachaErrores: String,
    val pistaPorPrecision: Boolean,
    val pistaPorRacha: Boolean
)

data class OperacionesConfiguracion(
    val ventanaIntentos: Int?,
    val alto: ActualizacionReglaAdaptativa?,
    val bajoPrecision: ActualizacionReglaAdaptativa?,
    val bajoRacha: ActualizacionReglaAdaptativa?
) {
    fun tieneCambios(): Boolean = ventanaIntentos != null || alto != null || bajoPrecision != null || bajoRacha != null
}

sealed interface ConfirmacionConfiguracion {
    val titulo: String
    val descripcion: String
    val cambios: List<String>

    data class Guardar(
        override val cambios: List<String>,
        val operaciones: OperacionesConfiguracion
    ) : ConfirmacionConfiguracion {
        override val titulo = "Confirmar cambios"
        override val descripcion = "Las siguientes decisiones adaptativas utilizarán los valores aplicados."
    }

    data object RestaurarTaller : ConfirmacionConfiguracion {
        override val titulo = "Restaurar valores del Taller"
        override val descripcion = "Se restaurarán ventana y umbrales certificados. No se modificarán intentos, progreso ni eventos."
        override val cambios = listOf("Ventana: 5 intentos", "R_ALTO: 80 % y 20 segundos", "Refuerzo: 40 % y 3 errores")
    }
}

data class ConfiguracionAdaptativaUiState(
    val carga: Carga<ConfiguracionAdaptativa> = Carga.Cargando,
    val borrador: BorradorConfiguracionAdaptativa? = null,
    val guardando: Boolean = false,
    val confirmacion: ConfirmacionConfiguracion? = null,
    val mensaje: String? = null
)

class ConfiguracionAdaptativaViewModel(
    private val repository: AdaptiveQuizRepository
) : ViewModel() {
    private val mutableEstado = MutableStateFlow(ConfiguracionAdaptativaUiState())
    val estado: StateFlow<ConfiguracionAdaptativaUiState> = mutableEstado.asStateFlow()

    fun cargar() = viewModelScope.launch {
        mutableEstado.value = mutableEstado.value.copy(carga = Carga.Cargando, mensaje = null)
        runCatching { repository.configuracionAdaptativa() }
            .onSuccess { publicarConfiguracion(it) }
            .onFailure { error ->
                mutableEstado.value = ConfiguracionAdaptativaUiState(carga = Carga.Error(error.mensajeAmigable()))
            }
    }

    fun actualizarVentana(valor: String) = actualizarBorrador { copy(ventanaIntentos = valor) }
    fun actualizarPrecisionAlto(valor: String) = actualizarBorrador { copy(precisionMinimaAlto = valor) }
    fun actualizarTiempoAlto(valor: String) = actualizarBorrador { copy(tiempoMaximoSegundos = valor) }
    fun actualizarPrecisionRefuerzo(valor: String) = actualizarBorrador { copy(precisionMaximaRefuerzo = valor) }
    fun actualizarRacha(valor: String) = actualizarBorrador { copy(rachaErrores = valor) }
    fun actualizarPistaPrecision(valor: Boolean) = actualizarBorrador { copy(pistaPorPrecision = valor) }
    fun actualizarPistaRacha(valor: Boolean) = actualizarBorrador { copy(pistaPorRacha = valor) }

    fun solicitarGuardar() {
        val configuracion = configuracionActual() ?: return
        val borrador = mutableEstado.value.borrador ?: return
        runCatching { crearOperaciones(configuracion, borrador) }
            .onSuccess { (operaciones, cambios) ->
                mutableEstado.value = if (operaciones.tieneCambios()) {
                    mutableEstado.value.copy(confirmacion = ConfirmacionConfiguracion.Guardar(cambios, operaciones), mensaje = null)
                } else {
                    mutableEstado.value.copy(mensaje = "No hay cambios pendientes por guardar.")
                }
            }
            .onFailure { error -> mutableEstado.value = mutableEstado.value.copy(mensaje = error.mensajeAmigable()) }
    }

    fun solicitarRestauracion() {
        mutableEstado.value = mutableEstado.value.copy(confirmacion = ConfirmacionConfiguracion.RestaurarTaller, mensaje = null)
    }

    fun cancelarConfirmacion() {
        mutableEstado.value = mutableEstado.value.copy(confirmacion = null)
    }

    fun confirmar() {
        val confirmacion = mutableEstado.value.confirmacion ?: return
        viewModelScope.launch {
            mutableEstado.value = mutableEstado.value.copy(guardando = true, confirmacion = null, mensaje = null)
            runCatching {
                when (confirmacion) {
                    is ConfirmacionConfiguracion.Guardar -> aplicar(confirmacion.operaciones)
                    ConfirmacionConfiguracion.RestaurarTaller -> repository.restaurarConfiguracionTaller()
                }
            }.onSuccess { configuracion ->
                val mensaje = when (confirmacion) {
                    is ConfirmacionConfiguracion.Guardar ->
                        "Configuración actualizada. Las siguientes decisiones adaptativas utilizarán estos valores."
                    ConfirmacionConfiguracion.RestaurarTaller ->
                        "Valores certificados del Taller restaurados."
                }
                publicarConfiguracion(configuracion, mensaje)
            }.onFailure { error ->
                mutableEstado.value = mutableEstado.value.copy(guardando = false, mensaje = error.mensajeAmigable())
            }
        }
    }

    fun limpiarMensaje() {
        mutableEstado.value = mutableEstado.value.copy(mensaje = null)
    }

    private suspend fun aplicar(operaciones: OperacionesConfiguracion): ConfiguracionAdaptativa {
        operaciones.ventanaIntentos?.let { repository.actualizarPoliticaAdaptativa(it) }
        operaciones.alto?.let { repository.actualizarReglaAdaptativa(REGLA_ALTO, it) }
        operaciones.bajoPrecision?.let { repository.actualizarReglaAdaptativa(REGLA_BAJO_PRECISION, it) }
        operaciones.bajoRacha?.let { repository.actualizarReglaAdaptativa(REGLA_BAJO_RACHA, it) }
        return repository.configuracionAdaptativa()
    }

    private fun publicarConfiguracion(configuracion: ConfiguracionAdaptativa, mensaje: String? = null) {
        mutableEstado.value = ConfiguracionAdaptativaUiState(
            carga = Carga.Exito(configuracion),
            borrador = borrador(configuracion),
            mensaje = mensaje
        )
    }

    private fun actualizarBorrador(transformacion: BorradorConfiguracionAdaptativa.() -> BorradorConfiguracionAdaptativa) {
        val actual = mutableEstado.value
        actual.borrador?.let { mutableEstado.value = actual.copy(borrador = it.transformacion(), mensaje = null) }
    }

    private fun configuracionActual(): ConfiguracionAdaptativa? =
        (mutableEstado.value.carga as? Carga.Exito<ConfiguracionAdaptativa>)?.datos

    private fun borrador(configuracion: ConfiguracionAdaptativa): BorradorConfiguracionAdaptativa {
        val alto = regla(configuracion, REGLA_ALTO)
        val bajoPrecision = regla(configuracion, REGLA_BAJO_PRECISION)
        val bajoRacha = regla(configuracion, REGLA_BAJO_RACHA)
        return BorradorConfiguracionAdaptativa(
            configuracion.politica.tamanoVentanaIntentos.toString(),
            porcentajeVisible(alto.porcentajeAciertoMin),
            ((alto.tiempoPromedioMaxMs ?: 0) / 1000).toString(),
            porcentajeVisible(bajoPrecision.porcentajeAciertoMax),
            (bajoRacha.rachaErroresMin ?: 0).toString(),
            bajoPrecision.habilitarPista,
            bajoRacha.habilitarPista
        )
    }

    private fun crearOperaciones(
        configuracion: ConfiguracionAdaptativa,
        borrador: BorradorConfiguracionAdaptativa
    ): Pair<OperacionesConfiguracion, List<String>> {
        val alto = regla(configuracion, REGLA_ALTO)
        val bajoPrecision = regla(configuracion, REGLA_BAJO_PRECISION)
        val bajoRacha = regla(configuracion, REGLA_BAJO_RACHA)
        val ventana = entero(borrador.ventanaIntentos, "La ventana de intentos")
        val precisionAlto = porcentaje(borrador.precisionMinimaAlto, "La precisión mínima para subir")
        val tiempoAltoMs = segundosAMilisegundos(borrador.tiempoMaximoSegundos)
        val precisionRefuerzo = porcentaje(borrador.precisionMaximaRefuerzo, "La precisión máxima de refuerzo")
        val racha = entero(borrador.rachaErrores, "Los errores consecutivos")
        val cambios = mutableListOf<String>()

        val ventanaCambio = ventana.takeIf { it != configuracion.politica.tamanoVentanaIntentos }
        ventanaCambio?.let { cambios += "Ventana de intentos\n${configuracion.politica.tamanoVentanaIntentos} → $it" }

        val altoCambio = ActualizacionReglaAdaptativa(
            porcentajeAciertoMin = precisionAlto.takeIf { it != alto.porcentajeAciertoMin },
            tiempoPromedioMaxMs = tiempoAltoMs.takeIf { it != alto.tiempoPromedioMaxMs }
        ).takeIf { it.tieneCambios() }
        altoCambio?.let {
            if (it.porcentajeAciertoMin != null) cambios += "Precisión mínima para subir\n${porcentajeVisible(alto.porcentajeAciertoMin)} % → ${porcentajeVisible(it.porcentajeAciertoMin)} %"
            if (it.tiempoPromedioMaxMs != null) cambios += "Tiempo promedio máximo\n${segundosVisibles(alto.tiempoPromedioMaxMs)} → ${segundosVisibles(it.tiempoPromedioMaxMs)}"
        }

        val bajoPrecisionCambio = ActualizacionReglaAdaptativa(
            porcentajeAciertoMax = precisionRefuerzo.takeIf { it != bajoPrecision.porcentajeAciertoMax },
            habilitarPista = borrador.pistaPorPrecision.takeIf { it != bajoPrecision.habilitarPista }
        ).takeIf { it.tieneCambios() }
        bajoPrecisionCambio?.let {
            if (it.porcentajeAciertoMax != null) cambios += "Precisión máxima antes de reforzar\n${porcentajeVisible(bajoPrecision.porcentajeAciertoMax)} % → ${porcentajeVisible(it.porcentajeAciertoMax)} %"
            if (it.habilitarPista != null) cambios += "Pista por precisión baja\n${siNo(bajoPrecision.habilitarPista)} → ${siNo(it.habilitarPista)}"
        }

        val bajoRachaCambio = ActualizacionReglaAdaptativa(
            rachaErroresMin = racha.takeIf { it != bajoRacha.rachaErroresMin },
            habilitarPista = borrador.pistaPorRacha.takeIf { it != bajoRacha.habilitarPista }
        ).takeIf { it.tieneCambios() }
        bajoRachaCambio?.let {
            if (it.rachaErroresMin != null) cambios += "Errores consecutivos\n${bajoRacha.rachaErroresMin} → ${it.rachaErroresMin}"
            if (it.habilitarPista != null) cambios += "Pista por racha de errores\n${siNo(bajoRacha.habilitarPista)} → ${siNo(it.habilitarPista)}"
        }

        return OperacionesConfiguracion(ventanaCambio, altoCambio, bajoPrecisionCambio, bajoRachaCambio) to cambios
    }

    private fun regla(configuracion: ConfiguracionAdaptativa, codigo: String): ReglaAdaptativa =
        configuracion.reglas.firstOrNull { it.codigo == codigo }
            ?: throw IllegalStateException("No se encontró la regla $codigo en la política activa")

    private fun entero(valor: String, etiqueta: String): Int = valor.trim().toIntOrNull()
        ?: throw IllegalArgumentException("$etiqueta debe ser un número entero")

    private fun porcentaje(valor: String, etiqueta: String): BigDecimal {
        val porcentaje = valor.trim().replace(',', '.').toBigDecimalOrNull()
            ?: throw IllegalArgumentException("$etiqueta debe ser un porcentaje válido")
        if (porcentaje < BigDecimal.ZERO || porcentaje > BigDecimal(100)) {
            throw IllegalArgumentException("$etiqueta debe estar entre 0 % y 100 %")
        }
        return porcentaje.movePointLeft(2)
    }

    private fun segundosAMilisegundos(valor: String): Int {
        val segundos = entero(valor, "El tiempo promedio máximo")
        if (segundos < 0 || segundos > Int.MAX_VALUE / 1000) {
            throw IllegalArgumentException("El tiempo promedio máximo debe ser un valor válido")
        }
        return segundos * 1000
    }

    private fun porcentajeVisible(valor: BigDecimal?): String =
        valor?.movePointRight(2)?.stripTrailingZeros()?.toPlainString() ?: "0"

    private fun segundosVisibles(valor: Int?): String = "${(valor ?: 0) / 1000} segundos"
    private fun siNo(valor: Boolean) = if (valor) "Sí" else "No"
}

private fun ActualizacionReglaAdaptativa.tieneCambios(): Boolean =
    porcentajeAciertoMin != null || porcentajeAciertoMax != null || tiempoPromedioMaxMs != null || rachaErroresMin != null || habilitarPista != null
