package com.veltia.adaptivequiz.application.usecase;

import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import com.veltia.adaptivequiz.domain.model.SesionPractica;

public record SesionIniciada(SesionPractica sesion, ProgresoTema progreso) {
}
