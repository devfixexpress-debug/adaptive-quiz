package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.Intento;

/** Contrato reservado para el flujo INTENTO_RESUELTO. */
public interface RegistrarIntentoUseCase {

    Intento registrar(Intento intento);
}
