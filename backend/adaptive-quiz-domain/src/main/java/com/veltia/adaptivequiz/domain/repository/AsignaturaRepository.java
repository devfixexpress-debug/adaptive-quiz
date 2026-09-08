package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.Asignatura;
import java.util.List;
import java.util.Optional;

/** Puerto de consulta, libre de Spring y JPA. */
public interface AsignaturaRepository {

    List<Asignatura> findAllActivas();

    Optional<Asignatura> findActivaById(Long idAsignatura);
}
