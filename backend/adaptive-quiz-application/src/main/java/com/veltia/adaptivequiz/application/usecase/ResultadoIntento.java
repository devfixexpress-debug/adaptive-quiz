package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;

public record ResultadoIntento(
        boolean correcto,
        String resultado,
        String explicacion,
        ProgresoTema progreso,
        EventoAdaptacion adaptacion,
        ContextoAprendizaje contexto
) {
}
