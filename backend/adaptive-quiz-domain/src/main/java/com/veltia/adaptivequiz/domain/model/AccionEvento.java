package com.veltia.adaptivequiz.domain.model;

/** Acción registrada o ejecutada para un evento adaptativo. */
public record AccionEvento(
        short secuencia,
        ItemCatalogo accionAdaptacion,
        String detalle,
        String valorAnterior,
        String valorNuevo,
        boolean ejecutada
) {
}
