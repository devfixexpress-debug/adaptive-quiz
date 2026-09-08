package com.veltia.adaptivequiz.domain.model;

public record Tema(
        Long idTema,
        Long idAsignatura,
        Long idTemaPadre,
        String codigo,
        String nombre,
        String descripcion,
        int orden,
        boolean activo
) {
}
