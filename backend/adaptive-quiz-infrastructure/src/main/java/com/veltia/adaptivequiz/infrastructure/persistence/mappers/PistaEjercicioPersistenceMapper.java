package com.veltia.adaptivequiz.infrastructure.persistence.mappers;

import com.veltia.adaptivequiz.domain.model.PistaEjercicio;
import com.veltia.adaptivequiz.infrastructure.persistence.entities.PistaEjercicioEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PistaEjercicioPersistenceMapper {

    PistaEjercicio toDomain(PistaEjercicioEntity entity);

    List<PistaEjercicio> toDomain(List<PistaEjercicioEntity> entities);
}
