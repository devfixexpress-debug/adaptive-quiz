package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProgresoTema(
        Long idProgresoTema,
        Long idEstudiante,
        Long idTema,
        ItemCatalogo dificultadActual,
        int totalIntentos,
        int totalAciertos,
        BigDecimal porcentajeAcierto,
        int tiempoPromedioMs,
        int rachaAciertosActual,
        int rachaErroresActual,
        OffsetDateTime fechaUltimoIntento
) {
}
