package com.veltia.adaptivequiz.domain.model;

public record Asignatura(
        Long idAsignatura,
        String codigo,
        String nombre,
        String descripcion,
        boolean activo
) {
}
