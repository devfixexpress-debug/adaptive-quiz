package com.veltia.adaptivequiz.infrastructure.persistence.jpa;

import com.veltia.adaptivequiz.infrastructure.persistence.entities.AsignaturaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignaturaJpaRepository extends JpaRepository<AsignaturaEntity, Long> {

    List<AsignaturaEntity> findByActivoTrueOrderByNombreAsc();

    Optional<AsignaturaEntity> findByIdAsignaturaAndActivoTrue(Long idAsignatura);
}
