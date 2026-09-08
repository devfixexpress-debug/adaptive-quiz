package com.veltia.adaptivequiz.infrastructure.persistence.mappers;

import com.veltia.adaptivequiz.domain.model.Asignatura;
import com.veltia.adaptivequiz.infrastructure.persistence.entities.AsignaturaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AsignaturaPersistenceMapper {

    Asignatura toDomain(AsignaturaEntity entity);
}
