package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import java.util.Objects;
import java.util.Optional;

/**
 * Une PROCESAMIENTO y DECISIÓN: recibe contexto y política, pide métricas al analizador y delega
 * la elección a una estrategia. Entrega una {@link AdaptationDecision} opcional; no consulta UI,
 * no persiste datos, no modifica dificultad directamente y no conoce proveedores de IA.
 */
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
