package com.veltia.adaptivequiz.api.mappers;

import com.veltia.adaptivequiz.api.dto.EjercicioResponse;
import com.veltia.adaptivequiz.api.dto.OpcionEjercicioResponse;
import com.veltia.adaptivequiz.api.dto.PistaEjercicioResponse;
import com.veltia.adaptivequiz.domain.model.Ejercicio;
import com.veltia.adaptivequiz.domain.model.OpcionEjercicio;
import com.veltia.adaptivequiz.domain.model.PistaEjercicio;
import org.springframework.stereotype.Component;

@Component
public class EjercicioApiMapper {

    public EjercicioResponse toResponse(Ejercicio ejercicio) {
        return new EjercicioResponse(
                ejercicio.idEjercicio(),
                ejercicio.idTema(),
                ejercicio.codigo(),
                ejercicio.enunciado(),
                ejercicio.tipoEjercicio().codigo(),
                ejercicio.dificultad().codigo(),
                ejercicio.estado().codigo(),
                ejercicio.tiempoObjetivoSegundos(),
                ejercicio.puntajeBase(),
                ejercicio.opciones().stream().map(this::toResponse).toList(),
                ejercicio.pistas().stream().map(this::toResponse).toList());
    }

    private OpcionEjercicioResponse toResponse(OpcionEjercicio opcion) {
        return new OpcionEjercicioResponse(
                opcion.idOpcionEjercicio(),
                opcion.codigo(),
                opcion.texto(),
                opcion.orden());
    }

    private PistaEjercicioResponse toResponse(PistaEjercicio pista) {
        return new PistaEjercicioResponse(
                pista.idPistaEjercicio(),
                pista.orden(),
                pista.texto(),
                pista.penalizacionPuntaje());
    }
}
