package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;

/** Parámetro activo de CFG_PARAMETRO, con valor tipado por su definición. */
public record ParametroConfiguracion(
        String codigo,
        Long valorEntero,
        BigDecimal valorDecimal,
        Boolean valorBooleano,
        String valorTexto
) {
}
