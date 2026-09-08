package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import java.util.Optional;

/**
 * Esqueleto de la estrategia determinista. La evaluación de CFG_REGLA_ADAPTACION se implementará
 * en M4; no contiene umbrales ni valores de catálogo embebidos.
 */
public final class RuleBasedAdaptationStrategy implements AdaptationStrategy {

    @Override
    public Optional<AdaptationDecision> decidir(ContextoAprendizaje contexto, PoliticaAdaptacion politica) {
        return Optional.empty();
    }
}
