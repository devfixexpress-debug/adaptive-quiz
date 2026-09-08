package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import java.util.Optional;

/**
 * Puerto de decisión. RuleBasedAdaptationStrategy es la estrategia certificable del Taller;
 * una futura AiAdaptationStrategy implementará este mismo contrato sin acoplar el motor a un proveedor.
 */
public interface AdaptationStrategy {

    Optional<AdaptationDecision> decidir(AnalisisRendimiento analisis, PoliticaAdaptacion politica);
}
