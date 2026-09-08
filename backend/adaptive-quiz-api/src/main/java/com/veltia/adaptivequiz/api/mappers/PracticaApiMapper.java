package com.veltia.adaptivequiz.api.mappers;

import com.veltia.adaptivequiz.api.dto.AccionAdaptacionResponse;
import com.veltia.adaptivequiz.api.dto.AdaptacionResponse;
import com.veltia.adaptivequiz.api.dto.ContextoAprendizajeResponse;
import com.veltia.adaptivequiz.api.dto.ProgresoTemaResponse;
import com.veltia.adaptivequiz.api.dto.ResultadoIntentoResponse;
import com.veltia.adaptivequiz.api.dto.SesionPracticaResponse;
import com.veltia.adaptivequiz.api.dto.SiguienteEjercicioResponse;
import com.veltia.adaptivequiz.application.usecase.DetalleAdaptacion;
import com.veltia.adaptivequiz.application.usecase.ResultadoIntento;
import com.veltia.adaptivequiz.application.usecase.SesionIniciada;
import com.veltia.adaptivequiz.application.usecase.SiguienteExperiencia;
import com.veltia.adaptivequiz.domain.model.AccionEvento;
import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import org.springframework.stereotype.Component;

@Component
public class PracticaApiMapper {

    private final EjercicioApiMapper ejercicioMapper;

    public PracticaApiMapper(EjercicioApiMapper ejercicioMapper) {
        this.ejercicioMapper = ejercicioMapper;
    }

    public SesionPracticaResponse toResponse(SesionIniciada iniciada) {
        var sesion = iniciada.sesion();
        return new SesionPracticaResponse(
                sesion.idSesionPractica(),
                sesion.idEstudiante(),
                sesion.idTema(),
                sesion.idPoliticaAdaptacion(),
                sesion.estado().codigo(),
                sesion.fechaInicio(),
                sesion.cantidadIntentos(),
                progreso(iniciada.progreso()));
    }

    public SiguienteEjercicioResponse toResponse(SiguienteExperiencia experiencia) {
        return new SiguienteEjercicioResponse(
                ejercicioMapper.toResponse(experiencia.ejercicio()),
                progreso(experiencia.progreso()),
                experiencia.pistaHabilitada(),
                experiencia.numeroPregunta(),
                experiencia.fallbackPorAgotamiento());
    }

    public ResultadoIntentoResponse toResponse(ResultadoIntento resultado) {
        return new ResultadoIntentoResponse(
                resultado.correcto(),
                resultado.resultado(),
                resultado.explicacion(),
                progreso(resultado.progreso()),
                adaptacion(resultado.adaptacion(), resultado.contexto()));
    }

    public ProgresoTemaResponse progreso(ProgresoTema progreso) {
        return new ProgresoTemaResponse(
                progreso.idProgresoTema(),
                progreso.idEstudiante(),
                progreso.idTema(),
                progreso.dificultadActual().codigo(),
                progreso.totalIntentos(),
                progreso.totalAciertos(),
                progreso.porcentajeAcierto(),
                progreso.tiempoPromedioMs(),
                progreso.rachaAciertosActual(),
                progreso.rachaErroresActual(),
                progreso.fechaUltimoIntento());
    }

    public AdaptacionResponse toResponse(DetalleAdaptacion detalle) {
        return adaptacion(detalle.evento(), detalle.contexto());
    }

    private AdaptacionResponse adaptacion(EventoAdaptacion evento, ContextoAprendizaje contexto) {
        boolean pista = evento.acciones().stream()
                .anyMatch(accion -> "ACTIVAR_PISTA".equals(accion.accionAdaptacion().codigo()) && accion.ejecutada());
        return new AdaptacionResponse(
                evento.idEventoAdaptacion(),
                evento.nivelRendimiento().codigo(),
                evento.codigoRegla(),
                evento.accionPrincipal().codigo(),
                evento.dificultadAnterior().codigo(),
                evento.dificultadNueva().codigo(),
                codigo(evento.tipoEjercicioAnterior()),
                codigo(evento.tipoEjercicioNuevo()),
                pista,
                evento.motivo(),
                evento.versionMotor(),
                evento.fechaDecision(),
                evento.aplicacionExitosa(),
                contexto == null ? null : contexto(contexto),
                evento.acciones().stream().map(this::accion).toList());
    }

    private ContextoAprendizajeResponse contexto(ContextoAprendizaje contexto) {
        return new ContextoAprendizajeResponse(
                contexto.idContextoAprendizaje(),
                contexto.idIntentoDisparador(),
                contexto.idEstudiante(),
                contexto.idTema(),
                contexto.numeroIntentosVentana(),
                contexto.totalAciertosVentana(),
                contexto.porcentajeAcierto(),
                contexto.tiempoPromedioMs(),
                contexto.rachaAciertos(),
                contexto.rachaErrores(),
                contexto.dificultadActual().codigo(),
                contexto.tipoEjercicioActual().codigo(),
                contexto.puntajeRendimiento());
    }

    private AccionAdaptacionResponse accion(AccionEvento accion) {
        return new AccionAdaptacionResponse(
                accion.secuencia(),
                accion.accionAdaptacion().codigo(),
                accion.detalle(),
                accion.valorAnterior(),
                accion.valorNuevo(),
                accion.ejecutada());
    }

    private String codigo(com.veltia.adaptivequiz.domain.model.ItemCatalogo item) {
        return item == null ? null : item.codigo();
    }
}
