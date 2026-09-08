package com.veltia.adaptivequiz.api.mappers;

import com.veltia.adaptivequiz.api.dto.ConfiguracionAdaptativaResponse;
import com.veltia.adaptivequiz.api.dto.PoliticaAdaptativaResponse;
import com.veltia.adaptivequiz.api.dto.ReglaAdaptativaResponse;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ReglaAdaptacion;
import org.springframework.stereotype.Component;

/** Separa el contrato REST de los modelos que conservan IDs y catálogos dentro del dominio. */
@Component
public class ConfiguracionAdaptativaApiMapper {

    public ConfiguracionAdaptativaResponse toResponse(PoliticaAdaptacion politica) {
        return new ConfiguracionAdaptativaResponse(
                new PoliticaAdaptativaResponse(
                        politica.codigo(),
                        politica.nombre(),
                        politica.versionPolitica(),
                        politica.tamanoVentanaIntentos()),
                politica.reglas().stream().map(this::regla).toList());
    }

    private ReglaAdaptativaResponse regla(ReglaAdaptacion regla) {
        return new ReglaAdaptativaResponse(
                regla.codigo(),
                regla.nombre(),
                regla.prioridad(),
                regla.porcentajeAciertoMin(),
                regla.porcentajeAciertoMax(),
                regla.tiempoPromedioMaxMs(),
                regla.rachaErroresMin() == null ? null : (int) regla.rachaErroresMin(),
                regla.habilitarPista());
    }
}
