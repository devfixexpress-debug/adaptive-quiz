package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import java.util.Optional;

public interface PoliticaAdaptacionRepository {

    Optional<PoliticaAdaptacion> findActivaVigente();

    Optional<PoliticaAdaptacion> findById(Long idPoliticaAdaptacion);
}
