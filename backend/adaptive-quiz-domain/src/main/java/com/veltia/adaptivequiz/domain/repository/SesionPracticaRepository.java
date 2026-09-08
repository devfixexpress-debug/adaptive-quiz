package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.SesionPractica;
import java.util.Optional;

public interface SesionPracticaRepository {

    Optional<SesionPractica> findById(Long idSesionPractica);

    Optional<SesionPractica> findIniciadaByEstudianteAndTema(Long idEstudiante, Long idTema);

    SesionPractica guardar(SesionPractica sesionPractica);
}
