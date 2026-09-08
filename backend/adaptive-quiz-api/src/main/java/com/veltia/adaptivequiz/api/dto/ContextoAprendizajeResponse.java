package com.veltia.adaptivequiz.api.dto;

import java.math.BigDecimal;

public record ContextoAprendizajeResponse(
        Long idContextoAprendizaje,
        Long intentoDisparadorId,
        Long estudianteId,
        Long temaId,
        int numeroIntentosVentana,
        int totalAciertosVentana,
        BigDecimal porcentajeAcierto,
        int tiempoPromedioMs,
        int rachaAciertos,
        int rachaErrores,
        String dificultadActual,
        String tipoEjercicioActual,
        BigDecimal puntajeRendimiento
) {
}
