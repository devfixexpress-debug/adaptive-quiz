package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;

public record DetalleAdaptacion(EventoAdaptacion evento, ContextoAprendizaje contexto) {
}
