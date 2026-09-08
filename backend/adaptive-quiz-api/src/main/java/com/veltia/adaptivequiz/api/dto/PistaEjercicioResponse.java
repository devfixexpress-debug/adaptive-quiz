package com.veltia.adaptivequiz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Pista ordenada asociada a un ejercicio")
public record PistaEjercicioResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") int orden,
        @Schema(example = "Aísla primero los términos que contienen la variable.") String texto,
        @Schema(example = "0.10") BigDecimal penalizacionPuntaje
) {
}
