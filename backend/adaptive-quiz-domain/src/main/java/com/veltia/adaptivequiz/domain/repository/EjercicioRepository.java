package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.Ejercicio;
import java.util.Optional;

/** Puerto que recupera el contenido de un ejercicio con sus opciones y pistas. */
public interface EjercicioRepository {

    Optional<Ejercicio> findDetalleById(Long idEjercicio);
}
