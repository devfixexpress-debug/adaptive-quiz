package com.veltia.adaptivequiz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tema académico activo perteneciente a una asignatura")
public record TemaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long idAsignatura,
        @Schema(example = "ALG") String codigo,
        @Schema(example = "Álgebra") String nombre,
        @Schema(example = "Ecuaciones y operaciones algebraicas") String descripcion,
        @Schema(example = "10") int orden
) {
}
