package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import java.util.List;
import java.util.Optional;

public interface ProgresoTemaRepository {

    Optional<ProgresoTema> findByEstudianteAndTema(Long idEstudiante, Long idTema);

    List<ProgresoTema> findByEstudiante(Long idEstudiante);

    ProgresoTema guardar(ProgresoTema progresoTema);
}
