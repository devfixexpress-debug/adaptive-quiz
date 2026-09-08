package com.veltia.adaptivequiz.api.dto;

public record SiguienteEjercicioResponse(
        EjercicioResponse ejercicio,
        ProgresoTemaResponse progreso,
        boolean pistaHabilitada,
        int numeroPregunta,
        boolean fallbackPorAgotamiento
) {
}
