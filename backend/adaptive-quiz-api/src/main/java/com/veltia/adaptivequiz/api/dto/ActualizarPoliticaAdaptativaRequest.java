package com.veltia.adaptivequiz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ActualizarPoliticaAdaptativaRequest(
        @Min(value = 1, message = "el tamaño de ventana debe ser al menos 1")
        @Max(value = 50, message = "el tamaño de ventana no puede superar 50")
        Integer tamanoVentanaIntentos
) {
}
