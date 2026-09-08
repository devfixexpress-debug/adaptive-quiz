package com.veltia.adaptivequiz.domain.repository;

import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import java.util.List;
import java.util.Optional;

public interface EventoAdaptacionRepository {

    EventoAdaptacion guardar(EventoAdaptacion eventoAdaptacion);

    List<EventoAdaptacion> findPorEstudiante(Long idEstudiante);

    Optional<EventoAdaptacion> findById(Long idEventoAdaptacion);

    Optional<EventoAdaptacion> findUltimoPorEstudianteAndTema(Long idEstudiante, Long idTema);
}
