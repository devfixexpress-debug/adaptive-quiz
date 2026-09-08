package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;

public interface ActualizarPoliticaAdaptativaUseCase {

    PoliticaAdaptacion actualizarPoliticaAdaptativa(ActualizarPoliticaAdaptativaCommand command);
}
