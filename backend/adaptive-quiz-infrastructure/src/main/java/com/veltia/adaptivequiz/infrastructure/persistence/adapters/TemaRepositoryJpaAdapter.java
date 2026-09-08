package com.veltia.adaptivequiz.infrastructure.persistence.adapters;

import com.veltia.adaptivequiz.domain.model.Tema;
import com.veltia.adaptivequiz.domain.repository.TemaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.jpa.TemaJpaRepository;
import com.veltia.adaptivequiz.infrastructure.persistence.mappers.TemaPersistenceMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TemaRepositoryJpaAdapter implements TemaRepository {

    private final TemaJpaRepository jpaRepository;
    private final TemaPersistenceMapper mapper;

    public TemaRepositoryJpaAdapter(TemaJpaRepository jpaRepository, TemaPersistenceMapper mapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository, "jpaRepository es obligatorio");
        this.mapper = Objects.requireNonNull(mapper, "mapper es obligatorio");
    }

    @Override
    public List<Tema> findActivosByAsignaturaId(Long idAsignatura) {
        return jpaRepository.findByIdAsignaturaAndActivoTrueOrderByOrdenAscNombreAsc(idAsignatura).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Tema> findActivoById(Long idTema) {
        return jpaRepository.findByIdTemaAndActivoTrue(idTema).map(mapper::toDomain);
    }
}
