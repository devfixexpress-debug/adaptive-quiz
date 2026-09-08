package com.veltia.adaptivequiz.infrastructure.persistence.adapters;

import com.veltia.adaptivequiz.domain.model.Asignatura;
import com.veltia.adaptivequiz.domain.repository.AsignaturaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.AsignaturaJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.mappers.AsignaturaPersistenceMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AsignaturaRepositoryJpaAdapter implements AsignaturaRepository {

    private final AsignaturaJpaRepository jpaRepository;
    private final AsignaturaPersistenceMapper mapper;

    public AsignaturaRepositoryJpaAdapter(AsignaturaJpaRepository jpaRepository, AsignaturaPersistenceMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository, "jpaRepository es obligatorio");
        this.mapper = Objects.requireNonNull(mapper, "mapper es obligatorio");
    }

    @Override
    public List<Asignatura> findAllActivas() {
        return jpaRepository.findByActivoTrueOrderByNombreAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Asignatura> findActivaById(Long idAsignatura) {
        return jpaRepository.findByIdAsignaturaAndActivoTrue(idAsignatura).map(mapper::toDomain);
    }
}
