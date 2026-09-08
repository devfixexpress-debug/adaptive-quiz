package com.veltia.adaptivequiz.infrastructure.persistence.jpa;

import com.veltia.adaptivequiz.infrastructure.persistence.entities.EjercicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjercicioJpaRepository extends JpaRepository<EjercicioEntity, Long> {
}
