package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.CambioReglaAdaptacion;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import java.util.Optional;

/** Puerto limitado para mantener umbrales de la política activa sin exponer un CRUD genérico. */
public interface ConfiguracionAdaptativaRepository {

    Optional<PoliticaAdaptacion> actualizarVentana(
            Long idPoliticaAdaptacion,
            short tamanoVentanaIntentos,
            String usuarioModificacion);

    Optional<PoliticaAdaptacion> actualizarRegla(
            Long idPoliticaAdaptacion,
            String codigoRegla,
            CambioReglaAdaptacion cambio,
            String usuarioModificacion);
}
