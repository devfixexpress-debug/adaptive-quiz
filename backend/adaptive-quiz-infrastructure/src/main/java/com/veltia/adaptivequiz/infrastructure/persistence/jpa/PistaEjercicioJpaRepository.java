package com.veltia.adaptivequiz.infrastructure.persistence.jpa;

import com.veltia.adaptivequiz.infrastructure.persistence.entities.PistaEjercicioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PistaEjercicioJpaRepository extends JpaRepository<PistaEjercicioEntity, Long> {

    List<PistaEjercicioEntity> findByIdEjercicioOrderByOrdenAsc(Long idEjercicio);
}
