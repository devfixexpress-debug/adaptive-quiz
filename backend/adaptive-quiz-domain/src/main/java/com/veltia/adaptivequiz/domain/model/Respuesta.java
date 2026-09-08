package com.veltia.adaptivequiz.domain.model;

import java.time.OffsetDateTime;
import java.util.List;

public record Respuesta(
        Long idRespuesta,
        Long idIntento,
        String textoRespuesta,
        List<Long> idOpcionesSeleccionadas,
        OffsetDateTime fechaRespuesta
) {
    public Respuesta {
        idOpcionesSeleccionadas = List.copyOf(idOpcionesSeleccionadas == null ? List.of() : idOpcionesSeleccionadas);
    }
}
