package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.AccionEvento;
import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.ReglaAdaptacion;
import java.util.List;

/**
 * Salida de la etapa DECISIÓN. Conserva contexto, regla aplicada, acción, límites resultantes y
 * motivo para que la etapa ADAPTACIÓN los persista como ADP_EVENTO_ADAPTACION. No ejecuta SQL, no
 * vuelve a evaluar umbrales ni conoce pantallas Android.
 */
public record AdaptationDecision(
        ContextoAprendizaje contexto,
        ReglaAdaptacion regla,
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

    public AdaptationDecision conContexto(ContextoAprendizaje contextoPersistido) {
        return new AdaptationDecision(
                contextoPersistido,
                regla,
                nivelRendimiento,
                accionPrincipal,
                dificultadAnterior,
                dificultadNueva,
                tipoEjercicioAnterior,
                tipoEjercicioNuevo,
                motivo,
                acciones);
    }
}
