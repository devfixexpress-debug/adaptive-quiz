package com.veltia.adaptivequiz.infrastructure.persistence.jpa;

import com.veltia.adaptivequiz.infrastructure.persistence.entities.OpcionEjercicioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpcionEjercicioJpaRepository extends JpaRepository<OpcionEjercicioEntity, Long> {

    List<OpcionEjercicioEntity> findByIdEjercicioOrderByOrdenAsc(Long idEjercicio);
}
