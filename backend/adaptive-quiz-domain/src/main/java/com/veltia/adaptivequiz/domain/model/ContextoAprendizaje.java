package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;

/**
 * Entrada auditable del pipeline adaptativo. Representa una ventana de intentos recientes con
 * precisión, tiempo, rachas y estado actual; la construye LearningContextBuilder y la consume el
 * analizador. No contiene reglas, no asigna dificultad y no decide ni persiste por sí misma.
 */
public record ContextoAprendizaje(
        Long idContextoAprendizaje,
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

    public ContextoAprendizaje conPuntajeRendimiento(BigDecimal nuevoPuntajeRendimiento) {
        return new ContextoAprendizaje(
                idContextoAprendizaje,
                idIntentoDisparador,
                idEstudiante,
                idTema,
                idPoliticaAdaptacion,
                numeroIntentosVentana,
                totalAciertosVentana,
                porcentajeAcierto,
                tiempoPromedioMs,
                rachaAciertos,
                rachaErrores,
                dificultadActual,
                tipoEjercicioActual,
                nuevoPuntajeRendimiento);
    }
}
