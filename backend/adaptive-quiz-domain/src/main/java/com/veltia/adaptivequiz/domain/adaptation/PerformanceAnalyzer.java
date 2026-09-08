package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;

/** Procesa métricas de un contexto sin acceder a UI, controller ni persistencia. */
public interface PerformanceAnalyzer {

    AnalisisRendimiento analizar(ContextoAprendizaje contexto);
}
