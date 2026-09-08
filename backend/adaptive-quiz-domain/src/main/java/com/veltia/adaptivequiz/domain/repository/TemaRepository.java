package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.Tema;
import java.util.List;
import java.util.Optional;

/** Puerto de consulta, libre de Spring y JPA. */
public interface TemaRepository {

    List<Tema> findActivosByAsignaturaId(Long idAsignatura);

    /** Consulta adicional compatible con los adaptadores de catálogo existentes. */
    default Optional<Tema> findActivoById(Long idTema) {
        return Optional.empty();
    }
}
