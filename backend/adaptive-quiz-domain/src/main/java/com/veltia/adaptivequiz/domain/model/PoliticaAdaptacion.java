package com.veltia.adaptivequiz.domain.model;

import java.time.OffsetDateTime;
import java.util.List;

public record PoliticaAdaptacion(
        Long idPoliticaAdaptacion,
        String codigo,
        String nombre,
        int versionPolitica,
        ItemCatalogo modo,
        short tamanoVentanaIntentos,
        OffsetDateTime vigenteDesde,
        OffsetDateTime vigenteHasta,
        boolean activa,
        List<ReglaAdaptacion> reglas
) {
    public PoliticaAdaptacion {
        reglas = List.copyOf(reglas == null ? List.of() : reglas);
    }
}
