package com.veltia.adaptivequiz.api.dto;

import java.math.BigDecimal;

/** Umbrales visibles de una regla; la API no expone su PK ni permite cambiar su semántica. */
public record ReglaAdaptativaResponse(
        String codigo,
        String nombre,
        int prioridad,
        BigDecimal porcentajeAciertoMin,
        BigDecimal porcentajeAciertoMax,
        Integer tiempoPromedioMaxMs,
        Integer rachaErroresMin,
        boolean habilitarPista
) {
}
