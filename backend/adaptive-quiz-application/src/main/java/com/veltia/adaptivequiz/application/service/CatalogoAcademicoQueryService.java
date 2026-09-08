package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.application.usecase.ConsultarAsignaturasUseCase;
import com.veltia.adaptivequiz.application.usecase.ConsultarTemasUseCase;
import com.veltia.adaptivequiz.domain.model.Asignatura;
import com.veltia.adaptivequiz.domain.model.Tema;
import com.veltia.adaptivequiz.domain.repository.AsignaturaRepository;
import com.veltia.adaptivequiz.domain.repository.TemaRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CatalogoAcademicoQueryService implements ConsultarAsignaturasUseCase, ConsultarTemasUseCase {

    private final AsignaturaRepository asignaturaRepository;
    private final TemaRepository temaRepository;

    public CatalogoAcademicoQueryService(AsignaturaRepository asignaturaRepository, TemaRepository temaRepository) {
        this.asignaturaRepository = Objects.requireNonNull(asignaturaRepository, "asignaturaRepository es obligatorio");
        this.temaRepository = Objects.requireNonNull(temaRepository, "temaRepository es obligatorio");
    }

    @Override
    public List<Asignatura> consultarAsignaturas() {
        return asignaturaRepository.findAllActivas();
    }

    @Override
    public List<Tema> consultarTemasDeAsignatura(Long idAsignatura) {
        Objects.requireNonNull(idAsignatura, "idAsignatura es obligatorio");
        asignaturaRepository.findActivaById(idAsignatura)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignatura", idAsignatura));
        return temaRepository.findActivosByAsignaturaId(idAsignatura);
    }
}
