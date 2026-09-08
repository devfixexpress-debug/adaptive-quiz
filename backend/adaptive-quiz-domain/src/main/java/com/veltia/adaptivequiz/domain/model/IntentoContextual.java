package com.veltia.adaptivequiz.domain.model;

import java.time.OffsetDateTime;

/** Proyección mínima de un intento usada exclusivamente para construir un snapshot de contexto. */
public record IntentoContextual(
        Long idIntento,
        boolean correcto,
        int tiempoRespuestaMs,
        ItemCatalogo dificultad,
        ItemCatalogo tipoEjercicio,
        OffsetDateTime fechaFin
) {
}
