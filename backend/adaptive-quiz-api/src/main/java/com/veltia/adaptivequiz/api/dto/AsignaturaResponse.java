package com.veltia.adaptivequiz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Asignatura académica disponible para práctica")
public record AsignaturaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "MAT") String codigo,
        @Schema(example = "Matemática") String nombre,
        @Schema(example = "Asignatura de demostración") String descripcion
) {
}
