package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/** Hecho inmutable de resolución: fuente de verdad del rendimiento. */
public record Intento(
        Long idIntento,
        Long idSesionPractica,
        Long idEjercicio,
        int numeroOrden,
        OffsetDateTime fechaInicio,
        OffsetDateTime fechaFin,
        int tiempoRespuestaMs,
        ItemCatalogo resultado,
        BigDecimal puntajeObtenido,
        boolean usoPista
) {
}
