package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import java.util.List;

/** Contrato reservado; aún no tiene adaptador REST ni implementación. */
public interface ConsultarAdaptacionUseCase {

    List<EventoAdaptacion> consultarPorEstudiante(Long idEstudiante);
}
