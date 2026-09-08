package com.veltia.adaptivequiz.api.mappers;

import com.veltia.adaptivequiz.api.dto.AsignaturaResponse;
import com.veltia.adaptivequiz.api.dto.TemaResponse;
import com.veltia.adaptivequiz.domain.model.Asignatura;
import com.veltia.adaptivequiz.domain.model.Tema;
import org.springframework.stereotype.Component;

@Component
public class CatalogoAcademicoApiMapper {

    public AsignaturaResponse toResponse(Asignatura asignatura) {
        return new AsignaturaResponse(
                asignatura.idAsignatura(),
                asignatura.codigo(),
                asignatura.nombre(),
                asignatura.descripcion());
    }

    public TemaResponse toResponse(Tema tema) {
        return new TemaResponse(
                tema.idTema(),
                tema.idAsignatura(),
                tema.codigo(),
                tema.nombre(),
                tema.descripcion(),
                tema.orden());
    }
}
