package com.veltia.adaptivequiz.api.dto;

public record ResultadoIntentoResponse(
        boolean correcto,
        String resultado,
        String explicacion,
        ProgresoTemaResponse progreso,
        AdaptacionResponse adaptacion
) {
}
