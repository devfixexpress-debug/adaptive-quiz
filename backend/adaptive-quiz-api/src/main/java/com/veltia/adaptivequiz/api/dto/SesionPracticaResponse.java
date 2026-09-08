package com.veltia.adaptivequiz.api.dto;

import java.time.OffsetDateTime;

public record SesionPracticaResponse(
        Long idSesionPractica,
        Long estudianteId,
        Long temaId,
        Long politicaAdaptacionId,
        String estado,
        OffsetDateTime fechaInicio,
        int cantidadIntentos,
        ProgresoTemaResponse progreso
) {
}
