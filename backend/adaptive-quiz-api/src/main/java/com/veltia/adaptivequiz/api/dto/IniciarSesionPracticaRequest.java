package com.veltia.adaptivequiz.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record IniciarSesionPracticaRequest(
        @NotNull @Positive Long estudianteId,
        @NotNull @Positive Long temaId
) {
}
