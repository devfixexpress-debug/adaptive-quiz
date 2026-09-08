package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;

/** Regla configurada en CFG_REGLA_ADAPTACION; la prioridad menor se evalúa primero. */
public record ReglaAdaptacion(
        Long idReglaAdaptacion,
        String codigo,
        String nombre,
        int prioridad,
        BigDecimal porcentajeAciertoMin,
        BigDecimal porcentajeAciertoMax,
        Integer tiempoPromedioMinMs,
        Integer tiempoPromedioMaxMs,
        Short rachaAciertosMin,
        Short rachaErroresMin,
        ItemCatalogo nivelRendimiento,
        ItemCatalogo accionPrincipal,
        ItemCatalogo tipoEjercicioDestino,
        boolean habilitarPista,
        boolean activa
) {
}
