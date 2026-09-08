package com.veltia.adaptivequiz.infrastructure.persistence.mappers;

import com.veltia.adaptivequiz.domain.model.Ejercicio;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.OpcionEjercicio;
import com.veltia.adaptivequiz.domain.model.PistaEjercicio;
import com.veltia.adaptivequiz.infrastructure.persistence.entities.CatalogoItemEntity;
import com.veltia.adaptivequiz.infrastructure.persistence.entities.EjercicioEntity;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/** Ensambla el detalle del ejercicio sin filtrar tipos o estados paramétricos en Java. */
@Component
public class EjercicioPersistenceMapper {

    private final OpcionEjercicioPersistenceMapper opcionMapper;
    private final PistaEjercicioPersistenceMapper pistaMapper;

    public EjercicioPersistenceMapper(
            OpcionEjercicioPersistenceMapper opcionMapper,
            PistaEjercicioPersistenceMapper pistaMapper) {
        this.opcionMapper = Objects.requireNonNull(opcionMapper, "opcionMapper es obligatorio");
        this.pistaMapper = Objects.requireNonNull(pistaMapper, "pistaMapper es obligatorio");
    }

    public Ejercicio toDomain(
            EjercicioEntity entity,
            List<com.veltia.adaptivequiz.infrastructure.persistence.entities.OpcionEjercicioEntity> opciones,
            List<com.veltia.adaptivequiz.infrastructure.persistence.entities.PistaEjercicioEntity> pistas) {
        return new Ejercicio(
                entity.getIdEjercicio(),
                entity.getIdTema(),
                entity.getCodigo(),
                entity.getEnunciado(),
                toItemCatalogo(entity.getTipoEjercicio()),
                toItemCatalogo(entity.getDificultad()),
                toItemCatalogo(entity.getEstado()),
                entity.getExplicacion(),
                entity.getTiempoObjetivoSegundos(),
                entity.getPuntajeBase(),
                opcionMapper.toDomain(opciones),
                pistaMapper.toDomain(pistas));
    }

    private ItemCatalogo toItemCatalogo(CatalogoItemEntity entity) {
        CatalogoItemEntity catalogoItem = Objects.requireNonNull(entity, "La referencia de catálogo es obligatoria");
        return new ItemCatalogo(catalogoItem.getIdItemCatalogo(), catalogoItem.getCodigo(), catalogoItem.getNombre());
    }
}
