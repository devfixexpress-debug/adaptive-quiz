package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.AccionEvento;
import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import java.util.List;

/** Resultado explicable de una estrategia; se persistirá como ADP_EVENTO_ADAPTACION. */
public record AdaptationDecision(
        ContextoAprendizaje contexto,
        ItemCatalogo nivelRendimiento,
        ItemCatalogo accionPrincipal,
        ItemCatalogo dificultadAnterior,
        ItemCatalogo dificultadNueva,
        ItemCatalogo tipoEjercicioAnterior,
        ItemCatalogo tipoEjercicioNuevo,
        String motivo,
        List<AccionEvento> acciones
) {
    public AdaptationDecision {
        acciones = List.copyOf(acciones == null ? List.of() : acciones);
    }
}
