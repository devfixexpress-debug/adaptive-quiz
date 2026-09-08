package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.AccionEvento;
import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;

/** Ejecuta y deja trazabilidad de las acciones derivadas de una decisión. */
public interface AdaptationActionExecutor {

    EventoAdaptacion ejecutar(AdaptationDecision decision);
}
