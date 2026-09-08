package com.veltia.adaptivequiz.infrastructure.persistence.adapters;

import com.veltia.adaptivequiz.domain.model.Ejercicio;
import com.veltia.adaptivequiz.domain.repository.EjercicioRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.EjercicioJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.OpcionEjercicioJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.PistaEjercicioJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.mappers.EjercicioPersistenceMapper;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

    @Override
    public Optional<Ejercicio> findSiguientePublicado(
            Long idTema,
            Long idItemDificultad,
            Long idItemTipoEjercicio,
            Set<Long> idEjerciciosExcluidos) {
        Set<Long> excluidos = idEjerciciosExcluidos == null ? Set.of() : Set.copyOf(idEjerciciosExcluidos);
        return ejercicioJpaRepository.findPublicadosPorTemaDificultadYTipo(
                        idTema, idItemDificultad, idItemTipoEjercicio)
                .stream()
                .filter(entity -> !excluidos.contains(entity.getIdEjercicio()))
                .findFirst()
                .map(entity -> mapper.toDomain(
                        entity,
                        opcionJpaRepository.findByIdEjercicioOrderByOrdenAsc(entity.getIdEjercicio()),
                        pistaJpaRepository.findByIdEjercicioOrderByOrdenAsc(entity.getIdEjercicio())));
    }

    @Override
    public Optional<Ejercicio> findPrimerPublicadoPorTema(Long idTema, Long idItemTipoEjercicio) {
        return ejercicioJpaRepository.findPublicadosPorTemaYTipo(idTema, idItemTipoEjercicio)
                .stream()
                .findFirst()
                .map(entity -> mapper.toDomain(
                        entity,
                        opcionJpaRepository.findByIdEjercicioOrderByOrdenAsc(entity.getIdEjercicio()),
                        pistaJpaRepository.findByIdEjercicioOrderByOrdenAsc(entity.getIdEjercicio())));
    }
}
