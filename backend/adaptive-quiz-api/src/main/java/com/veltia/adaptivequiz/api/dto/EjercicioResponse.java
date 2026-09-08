package com.veltia.adaptivequiz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Ejercicio con contenido visible para la práctica")
public record EjercicioResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long idTema,
        @Schema(example = "ALG-B-001") String codigo,
        @Schema(example = "2x + 4 = 12. ¿Cuánto vale x?") String enunciado,
        @Schema(example = "OPCION_UNICA") String tipoEjercicio,
        @Schema(example = "BASICO") String dificultad,
        @Schema(example = "PUBLICADO") String estado,
        @Schema(example = "20") Integer tiempoObjetivoSegundos,
        @Schema(example = "1.00") BigDecimal puntajeBase,
        List<OpcionEjercicioResponse> opciones,
        List<PistaEjercicioResponse> pistas
) {
}
