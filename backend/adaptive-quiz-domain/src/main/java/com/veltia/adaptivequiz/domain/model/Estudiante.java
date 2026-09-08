package com.veltia.adaptivequiz.domain.model;

public record Estudiante(
        Long idEstudiante,
        String codigo,
        String nombreMostrado,
        ItemCatalogo estado
) {
}
