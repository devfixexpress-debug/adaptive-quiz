package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Calcula métricas explicativas. No sustituye la evaluación determinista de CFG_REGLA_ADAPTACION.
 */
public final class DefaultPerformanceAnalyzer implements PerformanceAnalyzer {

    private static final BigDecimal CERO = BigDecimal.ZERO;
    private static final BigDecimal UNO = BigDecimal.ONE;

    private final BigDecimal pesoPrecision;
    private final BigDecimal pesoVelocidad;
    private final BigDecimal pesoConsistencia;
    private final int tiempoRapidoMs;

    public DefaultPerformanceAnalyzer(
            BigDecimal pesoPrecision,
            BigDecimal pesoVelocidad,
            BigDecimal pesoConsistencia,
            int tiempoRapidoMs) {
        this.pesoPrecision = Objects.requireNonNull(pesoPrecision, "pesoPrecision es obligatorio");
        this.pesoVelocidad = Objects.requireNonNull(pesoVelocidad, "pesoVelocidad es obligatorio");
        this.pesoConsistencia = Objects.requireNonNull(pesoConsistencia, "pesoConsistencia es obligatorio");
        if (tiempoRapidoMs <= 0) {
            throw new IllegalArgumentException("tiempoRapidoMs debe ser positivo");
        }
        this.tiempoRapidoMs = tiempoRapidoMs;
    }

    @Override
    public AnalisisRendimiento analizar(ContextoAprendizaje contexto) {
        Objects.requireNonNull(contexto, "contexto es obligatorio");
        BigDecimal velocidad = UNO.subtract(BigDecimal.valueOf(contexto.tiempoPromedioMs())
                .divide(BigDecimal.valueOf(tiempoRapidoMs), 6, RoundingMode.HALF_UP));
        velocidad = acotar(velocidad);

        BigDecimal consistencia = contexto.numeroIntentosVentana() == 0
                ? CERO
                : BigDecimal.valueOf(Math.max(contexto.rachaAciertos(), contexto.rachaErrores()))
                        .divide(BigDecimal.valueOf(contexto.numeroIntentosVentana()), 6, RoundingMode.HALF_UP);
        consistencia = acotar(consistencia);

        BigDecimal puntaje = contexto.porcentajeAcierto().multiply(pesoPrecision)
                .add(velocidad.multiply(pesoVelocidad))
                .add(consistencia.multiply(pesoConsistencia));
        return new AnalisisRendimiento(contexto, velocidad, consistencia, acotar(puntaje).setScale(6, RoundingMode.HALF_UP));
    }

    private BigDecimal acotar(BigDecimal valor) {
        return valor.max(CERO).min(UNO);
    }
}
