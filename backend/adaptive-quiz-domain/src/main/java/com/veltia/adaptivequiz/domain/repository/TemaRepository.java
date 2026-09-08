package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.Tema;
import java.util.List;

/** Puerto de consulta, libre de Spring y JPA. */
public interface TemaRepository {

    List<Tema> findActivosByAsignaturaId(Long idAsignatura);
}
