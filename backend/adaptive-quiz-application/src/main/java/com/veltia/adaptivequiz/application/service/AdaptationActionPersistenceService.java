package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.domain.adaptation.AdaptationActionExecutor;
import com.veltia.adaptivequiz.domain.adaptation.AdaptationDecision;
import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.ParametroConfiguracion;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import com.veltia.adaptivequiz.domain.repository.CatalogoRepository;
import com.veltia.adaptivequiz.domain.repository.EventoAdaptacionRepository;
import com.veltia.adaptivequiz.domain.repository.ParametroRepository;
import com.veltia.adaptivequiz.domain.repository.ProgresoTemaRepository;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** Ejecuta el cambio de progreso y persiste la decisión/auditoría en la misma transacción. */
@Service
public class AdaptationActionPersistenceService implements AdaptationActionExecutor {

    private static final String ORIGEN_REGLAS = "REGLAS";
    private static final String VERSION_MOTOR_REGLAS = "VERSION_MOTOR_REGLAS";

    private final ProgresoTemaRepository progresoTemaRepository;
    private final EventoAdaptacionRepository eventoAdaptacionRepository;
    private final CatalogoRepository catalogoRepository;
    private final ParametroRepository parametroRepository;

    public AdaptationActionPersistenceService(
            ProgresoTemaRepository progresoTemaRepository,
            EventoAdaptacionRepository eventoAdaptacionRepository,
            CatalogoRepository catalogoRepository,
            ParametroRepository parametroRepository) {
        this.progresoTemaRepository = Objects.requireNonNull(progresoTemaRepository, "progresoTemaRepository es obligatorio");
        this.eventoAdaptacionRepository = Objects.requireNonNull(eventoAdaptacionRepository, "eventoAdaptacionRepository es obligatorio");
        this.catalogoRepository = Objects.requireNonNull(catalogoRepository, "catalogoRepository es obligatorio");
        this.parametroRepository = Objects.requireNonNull(parametroRepository, "parametroRepository es obligatorio");
    }

    @Override
    public EventoAdaptacion ejecutar(AdaptationDecision decision) {
        if (decision.contexto().idContextoAprendizaje() == null) {
            throw new IllegalArgumentException("El contexto debe estar persistido antes de ejecutar una adaptación");
        }
        ProgresoTema progreso = progresoTemaRepository
                .findByEstudianteAndTema(decision.contexto().idEstudiante(), decision.contexto().idTema())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Progreso",
                        "estudiante=" + decision.contexto().idEstudiante() + ", tema=" + decision.contexto().idTema()));

        ProgresoTema progresoAdaptado = new ProgresoTema(
                progreso.idProgresoTema(),
                progreso.idEstudiante(),
                progreso.idTema(),
                decision.dificultadNueva(),
                progreso.totalIntentos(),
                progreso.totalAciertos(),
                progreso.porcentajeAcierto(),
                progreso.tiempoPromedioMs(),
                progreso.rachaAciertosActual(),
                progreso.rachaErroresActual(),
                progreso.fechaUltimoIntento());
        progresoTemaRepository.guardar(progresoAdaptado);

        ItemCatalogo origen = catalogoRepository.findItemActivo("ORIGEN_DECISION", ORIGEN_REGLAS)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ítem de catálogo", "ORIGEN_DECISION/REGLAS"));
        String versionMotor = parametroRepository.findActivoByCodigo(VERSION_MOTOR_REGLAS)
                .map(ParametroConfiguracion::valorTexto)
                .filter(valor -> valor != null && !valor.isBlank())
                .orElseThrow(() -> new RecursoNoEncontradoException("Parámetro", VERSION_MOTOR_REGLAS));

        EventoAdaptacion evento = new EventoAdaptacion(
                null,
                decision.contexto().idContextoAprendizaje(),
                decision.regla().idReglaAdaptacion(),
                decision.regla().codigo(),
                origen,
                decision.nivelRendimiento(),
                decision.accionPrincipal(),
                decision.dificultadAnterior(),
                decision.dificultadNueva(),
                decision.tipoEjercicioAnterior(),
                decision.tipoEjercicioNuevo(),
                decision.motivo(),
                versionMotor,
                OffsetDateTime.now(),
                true,
                decision.acciones());
        return eventoAdaptacionRepository.guardar(evento);
    }
}
