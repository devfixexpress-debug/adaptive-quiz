package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.application.usecase.ConsultarAdaptacionUseCase;
import com.veltia.adaptivequiz.application.usecase.ConsultarProgresoUseCase;
import com.veltia.adaptivequiz.application.usecase.DetalleAdaptacion;
import com.veltia.adaptivequiz.application.usecase.IniciarSesionPracticaCommand;
import com.veltia.adaptivequiz.application.usecase.IniciarSesionPracticaUseCase;
import com.veltia.adaptivequiz.application.usecase.ObtenerSiguienteEjercicioUseCase;
import com.veltia.adaptivequiz.application.usecase.RegistrarIntentoCommand;
import com.veltia.adaptivequiz.application.usecase.RegistrarIntentoUseCase;
import com.veltia.adaptivequiz.application.usecase.ResultadoIntento;
import com.veltia.adaptivequiz.application.usecase.SesionIniciada;
import com.veltia.adaptivequiz.application.usecase.SiguienteExperiencia;
import com.veltia.adaptivequiz.domain.adaptation.AdaptationActionExecutor;
import com.veltia.adaptivequiz.domain.adaptation.AdaptationDecision;
import com.veltia.adaptivequiz.domain.adaptation.AdaptationEngine;
import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.Ejercicio;
import com.veltia.adaptivequiz.domain.model.EventoAdaptacion;
import com.veltia.adaptivequiz.domain.model.Intento;
import com.veltia.adaptivequiz.domain.model.IntentoContextual;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.ParametroConfiguracion;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import com.veltia.adaptivequiz.domain.model.Respuesta;
import com.veltia.adaptivequiz.domain.model.SesionPractica;
import com.veltia.adaptivequiz.domain.model.Tema;
import com.veltia.adaptivequiz.domain.repository.CatalogoRepository;
import com.veltia.adaptivequiz.domain.repository.ContextoAprendizajeRepository;
import com.veltia.adaptivequiz.domain.repository.EjercicioRepository;
import com.veltia.adaptivequiz.domain.repository.EstudianteRepository;
import com.veltia.adaptivequiz.domain.repository.EventoAdaptacionRepository;
import com.veltia.adaptivequiz.domain.repository.IntentoRepository;
import com.veltia.adaptivequiz.domain.repository.ParametroRepository;
import com.veltia.adaptivequiz.domain.repository.PoliticaAdaptacionRepository;
import com.veltia.adaptivequiz.domain.repository.ProgresoTemaRepository;
import com.veltia.adaptivequiz.domain.repository.RespuestaRepository;
import com.veltia.adaptivequiz.domain.repository.SesionPracticaRepository;
import com.veltia.adaptivequiz.domain.repository.TemaRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orquesta el vertical slice de práctica. Recibe comandos de sesión e intento y devuelve el
 * resultado que Android debe mostrar. Reúne las etapas del pipeline, pero no define umbrales ni
 * decide dificultad por sí mismo: delega contexto, análisis, estrategia y persistencia a sus
 * responsabilidades especializadas.
 */
@Service
@Transactional
public class PracticaService implements
        IniciarSesionPracticaUseCase,
        ObtenerSiguienteEjercicioUseCase,
        RegistrarIntentoUseCase,
        ConsultarProgresoUseCase,
        ConsultarAdaptacionUseCase {

    private static final String DIFICULTAD_INICIAL = "DIFICULTAD_INICIAL_CODIGO";
    private static final String TIPO_INICIAL = "TIPO_EJERCICIO_INICIAL_CODIGO";
    private static final String ESTADO_SESION_INICIADA = "INICIADA";
    private static final String RESULTADO_CORRECTO = "CORRECTO";
    private static final String RESULTADO_INCORRECTO = "INCORRECTO";
    private static final String TIPO_OPCION_UNICA = "OPCION_UNICA";
    private static final String ACCION_ACTIVAR_PISTA = "ACTIVAR_PISTA";

    private final EstudianteRepository estudianteRepository;
    private final TemaRepository temaRepository;
    private final PoliticaAdaptacionRepository politicaAdaptacionRepository;
    private final CatalogoRepository catalogoRepository;
    private final ParametroRepository parametroRepository;
    private final ProgresoTemaRepository progresoTemaRepository;
    private final SesionPracticaRepository sesionPracticaRepository;
    private final EjercicioRepository ejercicioRepository;
    private final IntentoRepository intentoRepository;
    private final RespuestaRepository respuestaRepository;
    private final ContextoAprendizajeRepository contextoRepository;
    private final EventoAdaptacionRepository eventoRepository;
    private final LearningContextBuilder learningContextBuilder;
    private final MotorAdaptativoFactory motorAdaptativoFactory;
    private final AdaptationActionExecutor adaptationActionExecutor;

    public PracticaService(
            EstudianteRepository estudianteRepository,
            TemaRepository temaRepository,
            PoliticaAdaptacionRepository politicaAdaptacionRepository,
            CatalogoRepository catalogoRepository,
            ParametroRepository parametroRepository,
            ProgresoTemaRepository progresoTemaRepository,
            SesionPracticaRepository sesionPracticaRepository,
            EjercicioRepository ejercicioRepository,
            IntentoRepository intentoRepository,
            RespuestaRepository respuestaRepository,
            ContextoAprendizajeRepository contextoRepository,
            EventoAdaptacionRepository eventoRepository,
            LearningContextBuilder learningContextBuilder,
            MotorAdaptativoFactory motorAdaptativoFactory,
            AdaptationActionExecutor adaptationActionExecutor) {
        this.estudianteRepository = Objects.requireNonNull(estudianteRepository, "estudianteRepository es obligatorio");
        this.temaRepository = Objects.requireNonNull(temaRepository, "temaRepository es obligatorio");
        this.politicaAdaptacionRepository = Objects.requireNonNull(politicaAdaptacionRepository, "politicaAdaptacionRepository es obligatorio");
        this.catalogoRepository = Objects.requireNonNull(catalogoRepository, "catalogoRepository es obligatorio");
        this.parametroRepository = Objects.requireNonNull(parametroRepository, "parametroRepository es obligatorio");
        this.progresoTemaRepository = Objects.requireNonNull(progresoTemaRepository, "progresoTemaRepository es obligatorio");
        this.sesionPracticaRepository = Objects.requireNonNull(sesionPracticaRepository, "sesionPracticaRepository es obligatorio");
        this.ejercicioRepository = Objects.requireNonNull(ejercicioRepository, "ejercicioRepository es obligatorio");
        this.intentoRepository = Objects.requireNonNull(intentoRepository, "intentoRepository es obligatorio");
        this.respuestaRepository = Objects.requireNonNull(respuestaRepository, "respuestaRepository es obligatorio");
        this.contextoRepository = Objects.requireNonNull(contextoRepository, "contextoRepository es obligatorio");
        this.eventoRepository = Objects.requireNonNull(eventoRepository, "eventoRepository es obligatorio");
        this.learningContextBuilder = Objects.requireNonNull(learningContextBuilder, "learningContextBuilder es obligatorio");
        this.motorAdaptativoFactory = Objects.requireNonNull(motorAdaptativoFactory, "motorAdaptativoFactory es obligatorio");
        this.adaptationActionExecutor = Objects.requireNonNull(adaptationActionExecutor, "adaptationActionExecutor es obligatorio");
    }

    @Override
    public SesionIniciada iniciar(IniciarSesionPracticaCommand command) {
        validarIdentificadores(command.idEstudiante(), command.idTema());
        estudianteRepository.findActivoById(command.idEstudiante())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", command.idEstudiante()));
        temaActivo(command.idTema());
        PoliticaAdaptacion politica = politicaActiva();

        ProgresoTema progreso = progresoTemaRepository.findByEstudianteAndTema(command.idEstudiante(), command.idTema())
                .orElseGet(() -> progresoTemaRepository.guardar(nuevoProgreso(command.idEstudiante(), command.idTema())));
        ItemCatalogo estadoIniciada = item("ESTADO_SESION", ESTADO_SESION_INICIADA);
        SesionPractica sesion = sesionPracticaRepository.guardar(new SesionPractica(
                null,
                command.idEstudiante(),
                command.idTema(),
                politica.idPoliticaAdaptacion(),
                estadoIniciada,
                OffsetDateTime.now(),
                null,
                0));
        return new SesionIniciada(sesion, progreso);
    }

    @Override
    @Transactional(readOnly = true)
    public SiguienteExperiencia obtener(Long idEstudiante, Long idTema) {
        validarIdentificadores(idEstudiante, idTema);
        estudianteRepository.findActivoById(idEstudiante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", idEstudiante));
        temaActivo(idTema);
        SesionPractica sesion = sesionPracticaRepository.findIniciadaByEstudianteAndTema(idEstudiante, idTema)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sesión de práctica", "estudiante=" + idEstudiante + ", tema=" + idTema));
        ProgresoTema progreso = progresoTemaRepository.findByEstudianteAndTema(idEstudiante, idTema)
                .orElseThrow(() -> new RecursoNoEncontradoException("Progreso", "estudiante=" + idEstudiante + ", tema=" + idTema));
        ItemCatalogo tipo = tipoParaSiguienteExperiencia(idEstudiante, idTema);
        Set<Long> resueltos = new HashSet<>(intentoRepository.findIdsEjerciciosResueltosPorSesion(sesion.idSesionPractica()));
        Optional<Ejercicio> noRepetido = ejercicioRepository.findSiguientePublicado(
                idTema,
                progreso.dificultadActual().idItemCatalogo(),
                tipo.idItemCatalogo(),
                resueltos);
        boolean fallback = noRepetido.isEmpty();
        Ejercicio ejercicio = noRepetido
                .or(() -> ejercicioRepository.findSiguientePublicado(
                        idTema,
                        progreso.dificultadActual().idItemCatalogo(),
                        tipo.idItemCatalogo(),
                        Set.of()))
                .or(() -> ejercicioRepository.findPrimerPublicadoPorTema(idTema, tipo.idItemCatalogo()))
                .orElseThrow(() -> new RecursoNoEncontradoException("Ejercicio publicado", "tema=" + idTema));
        boolean pistaHabilitada = eventoRepository.findUltimoPorEstudianteAndTema(idEstudiante, idTema)
                .map(evento -> evento.acciones().stream()
                        .anyMatch(accion -> ACCION_ACTIVAR_PISTA.equals(accion.accionAdaptacion().codigo())
                                && accion.ejecutada()))
                .orElse(false);
        return new SiguienteExperiencia(
                ejercicio,
                progreso,
                pistaHabilitada,
                sesion.cantidadIntentos() + 1,
                fallback);
    }

    @Override
    public ResultadoIntento registrar(RegistrarIntentoCommand command) {
        // 1. Validar la sesión, el ejercicio y las opciones antes de alterar datos de práctica.
        validarRegistro(command);
        SesionPractica sesion = sesionPracticaRepository.findById(command.idSesionPractica())
                .orElseThrow(() -> new RecursoNoEncontradoException("Sesión de práctica", command.idSesionPractica()));
        validarSesionIniciada(sesion);
        Ejercicio ejercicio = ejercicioRepository.findDetalleById(command.idEjercicio())
                .orElseThrow(() -> new RecursoNoEncontradoException("Ejercicio", command.idEjercicio()));
        if (!sesion.idTema().equals(ejercicio.idTema())) {
            throw new IllegalArgumentException("El ejercicio no pertenece al tema de la sesión");
        }
        ProgresoTema progresoActual = progresoTemaRepository
                .findByEstudianteAndTema(sesion.idEstudiante(), sesion.idTema())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Progreso",
                        "estudiante=" + sesion.idEstudiante() + ", tema=" + sesion.idTema()));
        validarOpciones(ejercicio, command.idOpcionesSeleccionadas());

        // 2. Calificar y persistir el intento y la respuesta seleccionada.
        boolean correcto = esCorrecta(ejercicio, command.idOpcionesSeleccionadas());
        ItemCatalogo resultado = item(
                "RESULTADO_INTENTO",
                correcto ? RESULTADO_CORRECTO : RESULTADO_INCORRECTO);
        OffsetDateTime fechaFin = OffsetDateTime.now();
        OffsetDateTime fechaInicio = fechaFin.minus(command.tiempoRespuestaMs(), ChronoUnit.MILLIS);
        Intento intento = intentoRepository.guardar(new Intento(
                null,
                sesion.idSesionPractica(),
                ejercicio.idEjercicio(),
                sesion.cantidadIntentos() + 1,
                fechaInicio,
                fechaFin,
                command.tiempoRespuestaMs(),
                resultado,
                puntaje(ejercicio, correcto, command.usoPista()),
                command.usoPista()));
        respuestaRepository.guardar(new Respuesta(
                null,
                intento.idIntento(),
                null,
                command.idOpcionesSeleccionadas(),
                fechaFin));
        sesionPracticaRepository.guardar(new SesionPractica(
                sesion.idSesionPractica(),
                sesion.idEstudiante(),
                sesion.idTema(),
                sesion.idPoliticaAdaptacion(),
                sesion.estado(),
                sesion.fechaInicio(),
                sesion.fechaFin(),
                sesion.cantidadIntentos() + 1));

        // 3. Actualizar el resumen de progreso previo a la adaptación.
        ProgresoTema progresoResumen = actualizarResumen(progresoActual, correcto, command.tiempoRespuestaMs(), fechaFin);
        progresoTemaRepository.guardar(progresoResumen);

        // 4. Construir el snapshot con la ventana que indica la política persistida.
        PoliticaAdaptacion politica = politicaAdaptacionRepository.findById(sesion.idPoliticaAdaptacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Política de adaptación", sesion.idPoliticaAdaptacion()));
        ContextoAprendizaje contextoInicial = learningContextBuilder.construir(intento, politica, progresoResumen);

        // 5. Analizar rendimiento y 6. evaluar reglas configuradas por prioridad dentro del motor.
        AdaptationEngine motor = motorAdaptativoFactory.crearMotor();
        AdaptationDecision decision = motor.decidir(contextoInicial, politica)
                .orElseThrow(() -> new IllegalStateException("La política activa no produjo una decisión"));

        // 7. Persistir contexto y aplicar la adaptación auditable sobre el progreso.
        ContextoAprendizaje contextoPersistido = contextoRepository.guardar(decision.contexto());
        EventoAdaptacion evento = adaptationActionExecutor.ejecutar(decision.conContexto(contextoPersistido));
        ProgresoTema progresoFinal = progresoTemaRepository
                .findByEstudianteAndTema(sesion.idEstudiante(), sesion.idTema())
                .orElseThrow(() -> new IllegalStateException("El progreso actualizado no está disponible"));

        // 8. Retornar a Android el resultado, progreso y decisión explicable ya persistida.
        return new ResultadoIntento(
                correcto,
                resultado.codigo(),
                ejercicio.explicacion(),
                progresoFinal,
                evento,
                contextoPersistido);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgresoTema> consultar(Long idEstudiante, Long idTema) {
        return progresoTemaRepository.findByEstudianteAndTema(idEstudiante, idTema);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoTema> consultarPorEstudiante(Long idEstudiante) {
        estudianteRepository.findActivoById(idEstudiante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", idEstudiante));
        return progresoTemaRepository.findByEstudiante(idEstudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleAdaptacion> consultarAdaptacionesPorEstudiante(Long idEstudiante) {
        estudianteRepository.findActivoById(idEstudiante)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", idEstudiante));
        return eventoRepository.findPorEstudiante(idEstudiante).stream()
                .map(this::detalle)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleAdaptacion> consultarPorId(Long idEventoAdaptacion) {
        return eventoRepository.findById(idEventoAdaptacion).map(this::detalle);
    }

    private DetalleAdaptacion detalle(EventoAdaptacion evento) {
        ContextoAprendizaje contexto = contextoRepository.findContextoById(evento.idContextoAprendizaje())
                .orElseThrow(() -> new IllegalStateException("El contexto de una adaptación debe existir"));
        return new DetalleAdaptacion(evento, contexto);
    }

    private ProgresoTema nuevoProgreso(Long idEstudiante, Long idTema) {
        ItemCatalogo dificultadInicial = item(
                "DIFICULTAD",
                parametroTexto(DIFICULTAD_INICIAL));
        return new ProgresoTema(
                null,
                idEstudiante,
                idTema,
                dificultadInicial,
                0,
                0,
                BigDecimal.ZERO.setScale(4),
                0,
                0,
                0,
                null);
    }

    private ItemCatalogo tipoParaSiguienteExperiencia(Long idEstudiante, Long idTema) {
        List<IntentoContextual> recientes = intentoRepository.findRecientesParaContexto(idEstudiante, idTema, 1);
        if (!recientes.isEmpty()) {
            return recientes.get(0).tipoEjercicio();
        }
        return item("TIPO_EJERCICIO", parametroTexto(TIPO_INICIAL));
    }

    private ProgresoTema actualizarResumen(
            ProgresoTema actual,
            boolean correcto,
            int tiempoRespuestaMs,
            OffsetDateTime fechaIntento) {
        int totalIntentos = actual.totalIntentos() + 1;
        int totalAciertos = actual.totalAciertos() + (correcto ? 1 : 0);
        int tiempoPromedio = (int) Math.round(
                ((double) actual.tiempoPromedioMs() * actual.totalIntentos() + tiempoRespuestaMs) / totalIntentos);
        return new ProgresoTema(
                actual.idProgresoTema(),
                actual.idEstudiante(),
                actual.idTema(),
                actual.dificultadActual(),
                totalIntentos,
                totalAciertos,
                BigDecimal.valueOf(totalAciertos)
                        .divide(BigDecimal.valueOf(totalIntentos), 4, RoundingMode.HALF_UP),
                tiempoPromedio,
                correcto ? actual.rachaAciertosActual() + 1 : 0,
                correcto ? 0 : actual.rachaErroresActual() + 1,
                fechaIntento);
    }

    private BigDecimal puntaje(Ejercicio ejercicio, boolean correcto, boolean usoPista) {
        if (!correcto) {
            return BigDecimal.ZERO;
        }
        BigDecimal penalizacion = usoPista
                ? ejercicio.pistas().stream()
                        .map(pista -> pista.penalizacionPuntaje())
                        .findFirst()
                        .orElse(BigDecimal.ZERO)
                : BigDecimal.ZERO;
        return ejercicio.puntajeBase().subtract(penalizacion).max(BigDecimal.ZERO);
    }

    private void validarRegistro(RegistrarIntentoCommand command) {
        if (command.idSesionPractica() == null || command.idEjercicio() == null) {
            throw new IllegalArgumentException("sesionPracticaId y ejercicioId son obligatorios");
        }
        if (command.tiempoRespuestaMs() == null || command.tiempoRespuestaMs() < 0) {
            throw new IllegalArgumentException("tiempoRespuestaMs debe ser cero o positivo");
        }
        if (command.idOpcionesSeleccionadas().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos una opción");
        }
        if (new HashSet<>(command.idOpcionesSeleccionadas()).size() != command.idOpcionesSeleccionadas().size()) {
            throw new IllegalArgumentException("No se puede seleccionar una opción más de una vez");
        }
    }

    private void validarOpciones(Ejercicio ejercicio, List<Long> seleccionadas) {
        Set<Long> disponibles = ejercicio.opciones().stream()
                .map(opcion -> opcion.idOpcionEjercicio())
                .collect(java.util.stream.Collectors.toSet());
        if (!disponibles.containsAll(seleccionadas)) {
            throw new IllegalArgumentException("Las opciones seleccionadas no pertenecen al ejercicio");
        }
        if (TIPO_OPCION_UNICA.equals(ejercicio.tipoEjercicio().codigo()) && seleccionadas.size() != 1) {
            throw new IllegalArgumentException("Un ejercicio de opción única requiere exactamente una opción");
        }
    }

    private boolean esCorrecta(Ejercicio ejercicio, List<Long> seleccionadas) {
        Set<Long> correctas = ejercicio.opciones().stream()
                .filter(opcion -> opcion.esCorrecta())
                .map(opcion -> opcion.idOpcionEjercicio())
                .collect(java.util.stream.Collectors.toSet());
        return correctas.equals(Set.copyOf(seleccionadas));
    }

    private void validarSesionIniciada(SesionPractica sesion) {
        if (!ESTADO_SESION_INICIADA.equals(sesion.estado().codigo())) {
            throw new IllegalArgumentException("La sesión de práctica no está iniciada");
        }
    }

    private PoliticaAdaptacion politicaActiva() {
        return politicaAdaptacionRepository.findActivaVigente()
                .orElseThrow(() -> new RecursoNoEncontradoException("Política de adaptación", "activa vigente"));
    }

    private Tema temaActivo(Long idTema) {
        return temaRepository.findActivoById(idTema)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tema", idTema));
    }

    private ItemCatalogo item(String codigoCatalogo, String codigoItem) {
        return catalogoRepository.findItemActivo(codigoCatalogo, codigoItem)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ítem de catálogo", codigoCatalogo + "/" + codigoItem));
    }

    private String parametroTexto(String codigo) {
        return parametroRepository.findActivoByCodigo(codigo)
                .map(ParametroConfiguracion::valorTexto)
                .filter(valor -> valor != null && !valor.isBlank())
                .orElseThrow(() -> new RecursoNoEncontradoException("Parámetro", codigo));
    }

    private void validarIdentificadores(Long idEstudiante, Long idTema) {
        if (idEstudiante == null || idEstudiante <= 0 || idTema == null || idTema <= 0) {
            throw new IllegalArgumentException("estudianteId y temaId deben ser positivos");
        }
    }
}
