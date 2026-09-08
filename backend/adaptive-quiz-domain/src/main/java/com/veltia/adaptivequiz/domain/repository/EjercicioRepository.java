package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.Ejercicio;
import java.util.Set;
import java.util.Optional;

/** Puerto que recupera el contenido de un ejercicio con sus opciones y pistas. */
public interface EjercicioRepository {

    Optional<Ejercicio> findDetalleById(Long idEjercicio);

    Optional<Ejercicio> findSiguientePublicado(
            Long idTema,
            Long idItemDificultad,
            Long idItemTipoEjercicio,
            Set<Long> idEjerciciosExcluidos);

    Optional<Ejercicio> findPrimerPublicadoPorTema(Long idTema, Long idItemTipoEjercicio);
}
