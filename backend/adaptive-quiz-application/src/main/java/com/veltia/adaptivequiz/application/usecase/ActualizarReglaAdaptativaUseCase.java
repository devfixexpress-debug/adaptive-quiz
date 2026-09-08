package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;

public interface ActualizarReglaAdaptativaUseCase {

    PoliticaAdaptacion actualizarReglaAdaptativa(ActualizarReglaAdaptativaCommand command);
}
