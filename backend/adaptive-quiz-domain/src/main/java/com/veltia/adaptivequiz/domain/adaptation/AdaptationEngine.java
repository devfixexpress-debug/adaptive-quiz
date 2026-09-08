package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import java.util.Objects;
import java.util.Optional;

/** El motor sólo delega a una estrategia; no conoce IA, UI ni infraestructura. */
public final class AdaptationEngine {

    private final AdaptationStrategy strategy;

    public AdaptationEngine(AdaptationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy es obligatorio");
    }

    public Optional<AdaptationDecision> decidir(ContextoAprendizaje contexto, PoliticaAdaptacion politica) {
        return strategy.decidir(contexto, politica);
    }
}
