package com.veltia.adaptivequiz.domain.model;

import java.time.OffsetDateTime;

public record SesionPractica(
        Long idSesionPractica,
        Long idEstudiante,
        Long idTema,
        Long idPoliticaAdaptacion,
        ItemCatalogo estado,
        OffsetDateTime fechaInicio,
        OffsetDateTime fechaFin,
        int cantidadIntentos
) {
}
