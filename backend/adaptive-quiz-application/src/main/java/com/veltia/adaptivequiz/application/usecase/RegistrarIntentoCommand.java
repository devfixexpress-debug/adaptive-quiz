package com.veltia.adaptivequiz.application.usecase;

import java.util.List;

public record RegistrarIntentoCommand(
        Long idSesionPractica,
        Long idEjercicio,
        List<Long> idOpcionesSeleccionadas,
        Integer tiempoRespuestaMs,
        boolean usoPista
) {
    public RegistrarIntentoCommand {
        idOpcionesSeleccionadas = List.copyOf(idOpcionesSeleccionadas == null ? List.of() : idOpcionesSeleccionadas);
    }
}
