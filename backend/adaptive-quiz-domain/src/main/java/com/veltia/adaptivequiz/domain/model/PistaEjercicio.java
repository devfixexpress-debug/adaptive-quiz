package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;

public record PistaEjercicio(
        Long idPistaEjercicio,
        int orden,
        String texto,
        BigDecimal penalizacionPuntaje
) {
}
