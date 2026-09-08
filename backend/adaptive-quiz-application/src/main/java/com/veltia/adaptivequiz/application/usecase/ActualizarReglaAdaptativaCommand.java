package com.veltia.adaptivequiz.application.usecase;

import java.math.BigDecimal;

/** Comando parcial: sólo contiene los umbrales que un docente puede ajustar durante la demo. */
public record ActualizarReglaAdaptativaCommand(
        String codigoRegla,
        BigDecimal porcentajeAciertoMin,
        BigDecimal porcentajeAciertoMax,
        Integer tiempoPromedioMaxMs,
        Integer rachaErroresMin,
        Boolean habilitarPista
) {
}
