package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.AccionEvento;
import java.util.List;

/** Ejecutará y confirmará las acciones de una decisión en la fase funcional del motor. */
public interface AdaptationActionExecutor {

    List<AccionEvento> ejecutar(AdaptationDecision decision);
}
