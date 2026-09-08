package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.Ejercicio;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;

public record SiguienteExperiencia(
        Ejercicio ejercicio,
        ProgresoTema progreso,
        boolean pistaHabilitada,
        int numeroPregunta,
        boolean fallbackPorAgotamiento
) {
}
