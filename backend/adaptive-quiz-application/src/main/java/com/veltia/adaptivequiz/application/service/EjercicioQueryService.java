package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.application.usecase.ObtenerEjercicioUseCase;
import com.veltia.adaptivequiz.domain.model.Ejercicio;
import com.veltia.adaptivequiz.domain.repository.EjercicioRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EjercicioQueryService implements ObtenerEjercicioUseCase {

    private final EjercicioRepository ejercicioRepository;

    public EjercicioQueryService(EjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = Objects.requireNonNull(ejercicioRepository, "ejercicioRepository es obligatorio");
    }

    @Override
    public Ejercicio obtenerEjercicio(Long idEjercicio) {
        Objects.requireNonNull(idEjercicio, "idEjercicio es obligatorio");
        return ejercicioRepository.findDetalleById(idEjercicio)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ejercicio", idEjercicio));
    }
}
