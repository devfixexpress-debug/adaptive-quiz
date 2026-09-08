package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;

/** Snapshot auditable de métricas calculadas a partir de intentos recientes. */
public record ContextoAprendizaje(
        Long idIntentoDisparador,
        Long idEstudiante,
        Long idTema,
        Long idPoliticaAdaptacion,
        short numeroIntentosVentana,
        short totalAciertosVentana,
        BigDecimal porcentajeAcierto,
        int tiempoPromedioMs,
        short rachaAciertos,
        short rachaErrores,
        ItemCatalogo dificultadActual,
        ItemCatalogo tipoEjercicioActual,
        BigDecimal puntajeRendimiento
) {
}
