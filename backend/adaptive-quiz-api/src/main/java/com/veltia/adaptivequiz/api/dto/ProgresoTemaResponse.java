package com.veltia.adaptivequiz.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProgresoTemaResponse(
        Long idProgresoTema,
        Long estudianteId,
        Long temaId,
        String dificultadActual,
        int totalIntentos,
        int totalAciertos,
        BigDecimal porcentajeAcierto,
        int tiempoPromedioMs,
        int rachaAciertosActual,
        int rachaErroresActual,
        OffsetDateTime fechaUltimoIntento
) {
}
