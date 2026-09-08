package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import java.util.List;
import java.util.Optional;

public interface ConsultarAdaptacionUseCase {

    List<DetalleAdaptacion> consultarAdaptacionesPorEstudiante(Long idEstudiante);

    Optional<DetalleAdaptacion> consultarPorId(Long idEventoAdaptacion);
}
