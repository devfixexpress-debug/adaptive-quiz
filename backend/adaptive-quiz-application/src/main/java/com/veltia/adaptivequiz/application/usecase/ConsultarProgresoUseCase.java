package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import java.util.List;
import java.util.Optional;

public interface ConsultarProgresoUseCase {

    Optional<ProgresoTema> consultar(Long idEstudiante, Long idTema);

    List<ProgresoTema> consultarPorEstudiante(Long idEstudiante);
}
