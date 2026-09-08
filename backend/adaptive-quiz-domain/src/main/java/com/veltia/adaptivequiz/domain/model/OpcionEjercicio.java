package com.veltia.adaptivequiz.domain.model;

public record OpcionEjercicio(
        Long idOpcionEjercicio,
        String codigo,
        String texto,
        boolean esCorrecta,
        int orden,
        String retroalimentacion
) {
}
