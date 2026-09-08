package com.veltia.adaptivequiz.infrastructure.persistence.mappers;

import com.veltia.adaptivequiz.domain.model.OpcionEjercicio;
import com.veltia.adaptivequiz.infrastructure.persistence.entities.OpcionEjercicioEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OpcionEjercicioPersistenceMapper {

    OpcionEjercicio toDomain(OpcionEjercicioEntity entity);

    List<OpcionEjercicio> toDomain(List<OpcionEjercicioEntity> entities);
}
