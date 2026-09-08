package com.veltia.adaptivequiz.api.dto;

import java.util.List;

public record ConfiguracionAdaptativaResponse(
        PoliticaAdaptativaResponse politica,
        List<ReglaAdaptativaResponse> reglas
) {
    public ConfiguracionAdaptativaResponse {
        reglas = List.copyOf(reglas == null ? List.of() : reglas);
    }
}
