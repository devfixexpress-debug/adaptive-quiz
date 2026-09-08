package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.Estudiante;
import java.util.Optional;

public interface EstudianteRepository {

    Optional<Estudiante> findActivoById(Long idEstudiante);
}
