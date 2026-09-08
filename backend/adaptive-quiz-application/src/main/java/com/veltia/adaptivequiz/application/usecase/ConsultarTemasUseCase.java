package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.Tema;
import java.util.List;

public interface ConsultarTemasUseCase {

    List<Tema> consultarTemasDeAsignatura(Long idAsignatura);
}
