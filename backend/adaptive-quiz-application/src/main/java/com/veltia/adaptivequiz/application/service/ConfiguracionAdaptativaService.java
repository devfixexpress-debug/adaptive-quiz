package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.application.usecase.ActualizarPoliticaAdaptativaCommand;
import com.veltia.adaptivequiz.application.usecase.ActualizarPoliticaAdaptativaUseCase;
import com.veltia.adaptivequiz.application.usecase.ActualizarReglaAdaptativaCommand;
import com.veltia.adaptivequiz.application.usecase.ActualizarReglaAdaptativaUseCase;
import com.veltia.adaptivequiz.application.usecase.ConsultarConfiguracionAdaptativaUseCase;
import com.veltia.adaptivequiz.application.usecase.RestaurarConfiguracionAdaptativaUseCase;
import com.veltia.adaptivequiz.domain.model.CambioReglaAdaptacion;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ReglaAdaptacion;
import com.veltia.adaptivequiz.domain.repository.ConfiguracionAdaptativaRepository;
import com.veltia.adaptivequiz.domain.repository.PoliticaAdaptacionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso docente para modificar sólo umbrales ya modelados. Etapa previa al motor: cambia la
 * configuración persistida que la siguiente evaluación leerá, pero nunca asigna dificultad a un
 * estudiante ni toca intentos, progreso, eventos o catálogos.
 */
@Service
@Transactional
public class ConfiguracionAdaptativaService implements
        ConsultarConfiguracionAdaptativaUseCase,
        ActualizarReglaAdaptativaUseCase,
        ActualizarPoliticaAdaptativaUseCase,
        RestaurarConfiguracionAdaptativaUseCase {

    private static final String USUARIO_DEMO = "DOCENTE_DEMO";
    private static final int MAXIMO_VENTANA = 50;
    private static final int MAXIMO_RACHA = Short.MAX_VALUE;

    private final PoliticaAdaptacionRepository politicaRepository;
    private final ConfiguracionAdaptativaRepository configuracionRepository;

    public ConfiguracionAdaptativaService(
            PoliticaAdaptacionRepository politicaRepository,
            ConfiguracionAdaptativaRepository configuracionRepository) {
        this.politicaRepository = Objects.requireNonNull(politicaRepository, "politicaRepository es obligatorio");
        this.configuracionRepository = Objects.requireNonNull(configuracionRepository, "configuracionRepository es obligatorio");
    }

    @Override
    @Transactional(readOnly = true)
    public PoliticaAdaptacion consultarConfiguracionAdaptativa() {
        return politicaActiva();
    }

    @Override
    public PoliticaAdaptacion actualizarPoliticaAdaptativa(ActualizarPoliticaAdaptativaCommand command) {
        Objects.requireNonNull(command, "La actualización de política es obligatoria");
        if (command.tamanoVentanaIntentos() == null
                || command.tamanoVentanaIntentos() < 1
                || command.tamanoVentanaIntentos() > MAXIMO_VENTANA) {
            throw new IllegalArgumentException("El tamaño de ventana debe estar entre 1 y 50 intentos");
        }
        PoliticaAdaptacion politica = politicaActiva();
        return configuracionRepository.actualizarVentana(
                        politica.idPoliticaAdaptacion(),
                        command.tamanoVentanaIntentos().shortValue(),
                        USUARIO_DEMO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Política de adaptación", politica.codigo()));
    }

    @Override
    public PoliticaAdaptacion actualizarReglaAdaptativa(ActualizarReglaAdaptativaCommand command) {
        Objects.requireNonNull(command, "La actualización de regla es obligatoria");
        PoliticaAdaptacion politica = politicaActiva();
        ReglaAdaptacion reglaActual = reglaDe(politica, command.codigoRegla());
        CambioReglaAdaptacion cambio = cambio(command);
        validarReglaEditable(reglaActual, cambio);
        validarValores(reglaActual, cambio);
        return configuracionRepository.actualizarRegla(
                        politica.idPoliticaAdaptacion(),
                        reglaActual.codigo(),
                        cambio,
                        USUARIO_DEMO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Regla de adaptación", reglaActual.codigo()));
    }

    @Override
    public PoliticaAdaptacion restaurarValoresTaller() {
        PoliticaAdaptacion politica = politicaActiva();
        if (!ConfiguracionAdaptativaBaseline.POLITICA_BASE_TALLER.equals(politica.codigo())) {
            throw new IllegalArgumentException("Sólo se puede restaurar la política base certificada del Taller");
        }
        configuracionRepository.actualizarVentana(
                        politica.idPoliticaAdaptacion(),
                        ConfiguracionAdaptativaBaseline.TAMANO_VENTANA_INTENTOS,
                        USUARIO_DEMO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Política de adaptación", politica.codigo()));
        for (String codigoRegla : List.of(
                ConfiguracionAdaptativaBaseline.R_BAJO_RACHA,
                ConfiguracionAdaptativaBaseline.R_BAJO_PRECISION,
                ConfiguracionAdaptativaBaseline.R_ALTO)) {
            configuracionRepository.actualizarRegla(
                            politica.idPoliticaAdaptacion(),
                            codigoRegla,
                            ConfiguracionAdaptativaBaseline.regla(codigoRegla),
                            USUARIO_DEMO)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Regla de adaptación", codigoRegla));
        }
        return politicaRepository.findById(politica.idPoliticaAdaptacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Política de adaptación", politica.codigo()));
    }

    private PoliticaAdaptacion politicaActiva() {
        return politicaRepository.findActivaVigente()
                .orElseThrow(() -> new RecursoNoEncontradoException("Política de adaptación", "activa vigente"));
    }

    private ReglaAdaptacion reglaDe(PoliticaAdaptacion politica, String codigoRegla) {
        if (codigoRegla == null || codigoRegla.isBlank()) {
            throw new IllegalArgumentException("El código de regla es obligatorio");
        }
        return politica.reglas().stream()
                .filter(regla -> codigoRegla.equals(regla.codigo()))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Regla de adaptación", codigoRegla));
    }

    private CambioReglaAdaptacion cambio(ActualizarReglaAdaptativaCommand command) {
        if (command.rachaErroresMin() != null
                && (command.rachaErroresMin() < 0 || command.rachaErroresMin() > MAXIMO_RACHA)) {
            throw new IllegalArgumentException("La racha de errores debe estar entre 0 y " + MAXIMO_RACHA);
        }
        return new CambioReglaAdaptacion(
                command.porcentajeAciertoMin(),
                command.porcentajeAciertoMax(),
                command.tiempoPromedioMaxMs(),
                command.rachaErroresMin() == null ? null : command.rachaErroresMin().shortValue(),
                command.habilitarPista());
    }

    private void validarReglaEditable(ReglaAdaptacion regla, CambioReglaAdaptacion cambio) {
        if (!cambio.tieneCambios()) {
            throw new IllegalArgumentException("Debe indicar al menos un umbral configurable");
        }
        boolean permitido = switch (regla.codigo()) {
            case ConfiguracionAdaptativaBaseline.R_ALTO -> cambio.porcentajeAciertoMax() == null
                    && cambio.rachaErroresMin() == null;
            case ConfiguracionAdaptativaBaseline.R_BAJO_PRECISION -> cambio.porcentajeAciertoMin() == null
                    && cambio.tiempoPromedioMaxMs() == null
                    && cambio.rachaErroresMin() == null;
            case ConfiguracionAdaptativaBaseline.R_BAJO_RACHA -> cambio.porcentajeAciertoMin() == null
                    && cambio.porcentajeAciertoMax() == null
                    && cambio.tiempoPromedioMaxMs() == null;
            default -> false;
        };
        if (!permitido) {
            throw new IllegalArgumentException("Los campos solicitados no son configurables para la regla " + regla.codigo());
        }
    }

    private void validarValores(ReglaAdaptacion regla, CambioReglaAdaptacion cambio) {
        validarPorcentaje(cambio.porcentajeAciertoMin(), "La precisión mínima");
        validarPorcentaje(cambio.porcentajeAciertoMax(), "La precisión máxima");
        if (cambio.tiempoPromedioMaxMs() != null && cambio.tiempoPromedioMaxMs() < 0) {
            throw new IllegalArgumentException("El tiempo promedio máximo debe ser cero o positivo");
        }

        BigDecimal minimo = cambio.porcentajeAciertoMin() == null
                ? regla.porcentajeAciertoMin()
                : cambio.porcentajeAciertoMin();
        BigDecimal maximo = cambio.porcentajeAciertoMax() == null
                ? regla.porcentajeAciertoMax()
                : cambio.porcentajeAciertoMax();
        if (minimo != null && maximo != null && minimo.compareTo(maximo) > 0) {
            throw new IllegalArgumentException("La precisión mínima no puede ser mayor que la precisión máxima");
        }

        Integer tiempoMaximo = cambio.tiempoPromedioMaxMs() == null
                ? regla.tiempoPromedioMaxMs()
                : cambio.tiempoPromedioMaxMs();
        if (regla.tiempoPromedioMinMs() != null
                && tiempoMaximo != null
                && tiempoMaximo < regla.tiempoPromedioMinMs()) {
            throw new IllegalArgumentException("El tiempo promedio máximo no puede ser menor que el mínimo");
        }
    }

    private void validarPorcentaje(BigDecimal valor, String etiqueta) {
        if (valor != null && (valor.compareTo(BigDecimal.ZERO) < 0 || valor.compareTo(BigDecimal.ONE) > 0)) {
            throw new IllegalArgumentException(etiqueta + " debe estar entre 0 y 1");
        }
    }
}
