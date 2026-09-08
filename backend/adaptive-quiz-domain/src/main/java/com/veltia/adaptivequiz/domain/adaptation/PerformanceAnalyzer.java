package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;

/** Procesa un contexto para clasificar el rendimiento mediante un ítem de catálogo. */
public interface PerformanceAnalyzer {

    ItemCatalogo analizar(ContextoAprendizaje contexto);
}
