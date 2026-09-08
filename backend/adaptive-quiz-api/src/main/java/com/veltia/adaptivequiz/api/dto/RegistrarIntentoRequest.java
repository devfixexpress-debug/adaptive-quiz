package com.veltia.adaptivequiz.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record RegistrarIntentoRequest(
        @NotNull @Positive Long sesionPracticaId,
        @NotNull @Positive Long ejercicioId,
        @NotEmpty List<@NotNull @Positive Long> opcionesSeleccionadas,
        @NotNull @PositiveOrZero Integer tiempoRespuestaMs,
        Boolean usoPista
) {
}
