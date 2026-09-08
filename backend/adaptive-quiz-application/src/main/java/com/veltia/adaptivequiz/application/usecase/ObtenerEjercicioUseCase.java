package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.Ejercicio;

public interface ObtenerEjercicioUseCase {

    Ejercicio obtenerEjercicio(Long idEjercicio);
}
