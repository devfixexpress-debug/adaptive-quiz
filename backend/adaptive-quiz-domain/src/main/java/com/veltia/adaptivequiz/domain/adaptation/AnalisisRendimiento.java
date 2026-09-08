package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import java.math.BigDecimal;

/** Métricas derivadas para observabilidad; las reglas configuradas siguen decidiendo el resultado. */
public record AnalisisRendimiento(
        ContextoAprendizaje contexto,
        BigDecimal velocidadNormalizada,
        BigDecimal consistenciaNormalizada,
        BigDecimal puntajeRendimiento
) {

    public ContextoAprendizaje contextoAuditable() {
        return contexto.conPuntajeRendimiento(puntajeRendimiento);
    }
}
