package com.veltia.adaptivequiz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** No expone la clave ni la retroalimentación para preservar la calificación posterior. */
@Schema(description = "Alternativa visible de un ejercicio")
public record OpcionEjercicioResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "A") String codigo,
        @Schema(example = "4") String texto,
        @Schema(example = "1") int orden
) {
}
