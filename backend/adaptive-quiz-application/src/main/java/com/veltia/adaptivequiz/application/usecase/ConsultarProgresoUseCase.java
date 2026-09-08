package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import java.util.Optional;

/** Contrato reservado; aún no tiene adaptador REST ni implementación. */
public interface ConsultarProgresoUseCase {

    Optional<ProgresoTema> consultar(Long idEstudiante, Long idTema);
}
