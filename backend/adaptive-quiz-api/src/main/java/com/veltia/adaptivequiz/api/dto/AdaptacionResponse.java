package com.veltia.adaptivequiz.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record AdaptacionResponse(
        Long idAdaptacion,
        String rendimiento,
        String regla,
        String accionPrincipal,
        String dificultadAnterior,
        String dificultadNueva,
        String tipoEjercicioAnterior,
        String tipoEjercicioNuevo,
        boolean pistaHabilitada,
        String motivo,
        String versionMotor,
        OffsetDateTime fechaDecision,
        boolean aplicacionExitosa,
        ContextoAprendizajeResponse contexto,
        List<AccionAdaptacionResponse> acciones
) {
}
