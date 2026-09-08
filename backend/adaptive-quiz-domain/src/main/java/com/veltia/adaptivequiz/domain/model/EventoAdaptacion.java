package com.veltia.adaptivequiz.domain.model;

import java.time.OffsetDateTime;
import java.util.List;

public record EventoAdaptacion(
        Long idEventoAdaptacion,
        Long idContextoAprendizaje,
        Long idReglaAdaptacion,
        ItemCatalogo origenDecision,
        ItemCatalogo nivelRendimiento,
        ItemCatalogo accionPrincipal,
        ItemCatalogo dificultadAnterior,
        ItemCatalogo dificultadNueva,
        ItemCatalogo tipoEjercicioAnterior,
        ItemCatalogo tipoEjercicioNuevo,
        String motivo,
        String versionMotor,
        OffsetDateTime fechaDecision,
        boolean aplicacionExitosa,
        List<AccionEvento> acciones
) {
    public EventoAdaptacion {
        acciones = List.copyOf(acciones == null ? List.of() : acciones);
    }
}
