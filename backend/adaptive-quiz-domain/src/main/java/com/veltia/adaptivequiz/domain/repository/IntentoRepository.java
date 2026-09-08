package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.Intento;
import com.veltia.adaptivequiz.domain.model.IntentoContextual;
import java.util.List;

public interface IntentoRepository {

    Intento guardar(Intento intento);

    List<IntentoContextual> findRecientesParaContexto(Long idEstudiante, Long idTema, int limite);

    List<Long> findIdsEjerciciosResueltosPorSesion(Long idSesionPractica);
}
