package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.domain.model.CambioReglaAdaptacion;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Única fuente en código de los valores certificados que puede restaurar la demostración. No se
 * usa para decidir el desempeño: las decisiones siempre leen la política y reglas persistidas.
 */
public final class ConfiguracionAdaptativaBaseline {

    public static final String POLITICA_BASE_TALLER = "POLITICA_BASE_TALLER";
    public static final short TAMANO_VENTANA_INTENTOS = 5;
    public static final String R_BAJO_RACHA = "R_BAJO_RACHA";
    public static final String R_BAJO_PRECISION = "R_BAJO_PRECISION";
    public static final String R_ALTO = "R_ALTO";

    private static final Map<String, CambioReglaAdaptacion> REGLAS = Map.of(
            R_BAJO_RACHA, new CambioReglaAdaptacion(null, null, null, (short) 3, true),
            R_BAJO_PRECISION, new CambioReglaAdaptacion(null, new BigDecimal("0.40"), null, null, true),
            R_ALTO, new CambioReglaAdaptacion(new BigDecimal("0.80"), null, 20000, null, false));

    private ConfiguracionAdaptativaBaseline() {
    }

    public static CambioReglaAdaptacion regla(String codigoRegla) {
        CambioReglaAdaptacion regla = REGLAS.get(codigoRegla);
        if (regla == null) {
            throw new IllegalArgumentException("La regla " + codigoRegla + " no pertenece al baseline del Taller");
        }
        return regla;
    }
}
