package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.SesionPractica;

/** Contrato reservado; aún no tiene adaptador REST ni implementación. */
public interface IniciarSesionPracticaUseCase {

    SesionPractica iniciar(Long idEstudiante, Long idTema);
}
