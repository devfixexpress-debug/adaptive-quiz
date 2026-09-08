package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;

/**
 * Valores editables de una regla ya existente. No representa un CRUD de la regla: conserva su
 * código, prioridad, acción, nivel y catálogos, que son parte de la semántica certificada.
 */
public record CambioReglaAdaptacion(
        BigDecimal porcentajeAciertoMin,
        BigDecimal porcentajeAciertoMax,
        Integer tiempoPromedioMaxMs,
        Short rachaErroresMin,
        Boolean habilitarPista
) {

    public boolean tieneCambios() {
        return porcentajeAciertoMin != null
                || porcentajeAciertoMax != null
                || tiempoPromedioMaxMs != null
                || rachaErroresMin != null
                || habilitarPista != null;
    }
}
