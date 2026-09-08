package com.veltia.adaptivequiz.api.dto;

/** Vista docente sin identificadores técnicos de la política activa. */
public record PoliticaAdaptativaResponse(
        String codigo,
        String nombre,
        int version,
        int tamanoVentanaIntentos
) {
}
