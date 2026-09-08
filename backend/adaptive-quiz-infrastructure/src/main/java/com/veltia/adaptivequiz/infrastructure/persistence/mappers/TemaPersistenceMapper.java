package com.veltia.adaptivequiz.infrastructure.persistence.mappers;

import com.veltia.adaptivequiz.domain.model.Tema;
import com.veltia.adaptivequiz.infrastructure.persistence.entities.TemaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TemaPersistenceMapper {

    Tema toDomain(TemaEntity entity);
}
