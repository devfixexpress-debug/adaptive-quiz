package com.veltia.adaptivequiz.infrastructure.persistence.jpa;

import com.veltia.adaptivequiz.infrastructure.persistence.entities.TemaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemaJpaRepository extends JpaRepository<TemaEntity, Long> {

    List<TemaEntity> findByIdAsignaturaAndActivoTrueOrderByOrdenAscNombreAsc(Long idAsignatura);

    Optional<TemaEntity> findByIdTemaAndActivoTrue(Long idTema);
}
