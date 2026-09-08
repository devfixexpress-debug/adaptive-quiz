package com.veltia.adaptivequiz.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/** Solicitud parcial y limitada a umbrales permitidos de una regla existente. */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ActualizarReglaAdaptativaRequest(
        @DecimalMin(value = "0.0", message = "la precisión mínima debe ser mayor o igual que 0")
        @DecimalMax(value = "1.0", message = "la precisión mínima debe ser menor o igual que 1")
        BigDecimal porcentajeAciertoMin,
        @DecimalMin(value = "0.0", message = "la precisión máxima debe ser mayor o igual que 0")
        @DecimalMax(value = "1.0", message = "la precisión máxima debe ser menor o igual que 1")
        BigDecimal porcentajeAciertoMax,
        @PositiveOrZero(message = "el tiempo promedio máximo debe ser cero o positivo")
        Integer tiempoPromedioMaxMs,
        @PositiveOrZero(message = "la racha de errores debe ser cero o positiva")
        Integer rachaErroresMin,
        Boolean habilitarPista
) {
}
