package com.veltia.adaptivequiz.infrastructure.persistence.adapters;

import com.veltia.adaptivequiz.domain.model.Ejercicio;
import com.veltia.adaptivequiz.domain.repository.EjercicioRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.EjercicioJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.OpcionEjercicioJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.PistaEjercicioJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.mappers.EjercicioPersistenceMapper;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class EjercicioRepositoryJpaAdapter implements EjercicioRepository {

    private final EjercicioJpaRepository ejercicioJpaRepository;
    private final OpcionEjercicioJpaRepository opcionJpaRepository;
    private final PistaEjercicioJpaRepository pistaJpaRepository;
    private final EjercicioPersistenceMapper mapper;

    public EjercicioRepositoryJpaAdapter(
            EjercicioJpaRepository ejercicioJpaRepository,
            OpcionEjercicioJpaRepository opcionJpaRepository,
            PistaEjercicioJpaRepository pistaJpaRepository,
            EjercicioPersistenceMapper mapper) {
        this.ejercicioJpaRepository = Objects.requireNonNull(ejercicioJpaRepository, "ejercicioJpaRepository es obligatorio");
        this.opcionJpaRepository = Objects.requireNonNull(opcionJpaRepository, "opcionJpaRepository es obligatorio");
        this.pistaJpaRepository = Objects.requireNonNull(pistaJpaRepository, "pistaJpaRepository es obligatorio");
        this.mapper = Objects.requireNonNull(mapper, "mapper es obligatorio");
    }

    @Override
    public Optional<Ejercicio> findDetalleById(Long idEjercicio) {
        return ejercicioJpaRepository.findById(idEjercicio)
                .map(entity -> mapper.toDomain(
                        entity,
                        opcionJpaRepository.findByIdEjercicioOrderByOrdenAsc(idEjercicio),
                        pistaJpaRepository.findByIdEjercicioOrderByOrdenAsc(idEjercicio)));
    }
}
