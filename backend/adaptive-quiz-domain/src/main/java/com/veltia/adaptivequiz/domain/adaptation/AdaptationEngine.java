package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import java.util.Objects;
import java.util.Optional;

/** El motor sólo delega a una estrategia; no conoce IA, UI ni infraestructura. */
public final class AdaptationEngine {

    private final PerformanceAnalyzer performanceAnalyzer;
    private final AdaptationStrategy strategy;

    public AdaptationEngine(PerformanceAnalyzer performanceAnalyzer, AdaptationStrategy strategy) {
        this.performanceAnalyzer = Objects.requireNonNull(performanceAnalyzer, "performanceAnalyzer es obligatorio");
        this.strategy = Objects.requireNonNull(strategy, "strategy es obligatorio");
    }

    public Optional<AdaptationDecision> decidir(ContextoAprendizaje contexto, PoliticaAdaptacion politica) {
        return strategy.decidir(performanceAnalyzer.analizar(contexto), politica);
    }
}
