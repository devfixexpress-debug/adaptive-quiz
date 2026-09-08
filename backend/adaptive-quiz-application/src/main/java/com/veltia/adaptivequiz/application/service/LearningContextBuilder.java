package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.Intento;
import com.veltia.adaptivequiz.domain.model.IntentoContextual;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import com.veltia.adaptivequiz.domain.repository.IntentoRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** Construye snapshots auditables a partir de la ventana reciente de intentos reales. */
@Service
public class LearningContextBuilder {

    private final IntentoRepository intentoRepository;

    public LearningContextBuilder(IntentoRepository intentoRepository) {
        this.intentoRepository = Objects.requireNonNull(intentoRepository, "intentoRepository es obligatorio");
    }

    public ContextoAprendizaje construir(Intento intentoDisparador, PoliticaAdaptacion politica, ProgresoTema progresoActual) {
        Objects.requireNonNull(intentoDisparador, "intentoDisparador es obligatorio");
        Objects.requireNonNull(politica, "politica es obligatoria");
        Objects.requireNonNull(progresoActual, "progresoActual es obligatorio");

        List<IntentoContextual> ventana = intentoRepository.findRecientesParaContexto(
                progresoActual.idEstudiante(),
                progresoActual.idTema(),
                politica.tamanoVentanaIntentos());
        if (ventana.isEmpty()) {
            throw new IllegalStateException("No existen intentos para construir el contexto");
        }

        long aciertos = ventana.stream().filter(IntentoContextual::correcto).count();
        int tiempoPromedio = (int) Math.round(ventana.stream()
                .mapToInt(IntentoContextual::tiempoRespuestaMs)
                .average()
                .orElse(0));
        short rachaAciertos = calcularRacha(ventana, true);
        short rachaErrores = calcularRacha(ventana, false);
        IntentoContextual masReciente = ventana.get(0);

        return new ContextoAprendizaje(
                null,
                intentoDisparador.idIntento(),
                progresoActual.idEstudiante(),
                progresoActual.idTema(),
                politica.idPoliticaAdaptacion(),
                (short) ventana.size(),
                (short) aciertos,
                BigDecimal.valueOf(aciertos)
                        .divide(BigDecimal.valueOf(ventana.size()), 4, RoundingMode.HALF_UP),
                tiempoPromedio,
                rachaAciertos,
                rachaErrores,
                progresoActual.dificultadActual(),
                masReciente.tipoEjercicio(),
                null);
    }

    private short calcularRacha(List<IntentoContextual> intentosOrdenadosDesc, boolean resultadoEsperado) {
        short racha = 0;
        for (IntentoContextual intento : intentosOrdenadosDesc) {
            if (intento.correcto() != resultadoEsperado) {
                break;
            }
            racha++;
        }
        return racha;
    }
}
