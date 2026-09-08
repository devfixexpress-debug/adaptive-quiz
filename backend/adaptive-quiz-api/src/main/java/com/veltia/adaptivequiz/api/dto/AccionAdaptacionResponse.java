package com.veltia.adaptivequiz.api.dto;

public record AccionAdaptacionResponse(
        int secuencia,
        String accion,
        String detalle,
        String valorAnterior,
        String valorNuevo,
        boolean ejecutada
) {
}
