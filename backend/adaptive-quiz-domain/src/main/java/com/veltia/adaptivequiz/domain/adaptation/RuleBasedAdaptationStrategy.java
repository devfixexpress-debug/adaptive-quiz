package com.veltia.adaptivequiz.domain.adaptation;

import com.veltia.adaptivequiz.domain.model.AccionEvento;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ReglaAdaptacion;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Estrategia determinista: evalúa reglas configuradas por prioridad ascendente y no depende de UI,
 * JPA ni IA.
 */
public final class RuleBasedAdaptationStrategy implements AdaptationStrategy {

    private static final String SUBIR_DIFICULTAD = "SUBIR_DIFICULTAD";
    private static final String BAJAR_DIFICULTAD = "BAJAR_DIFICULTAD";

    private final List<ItemCatalogo> escalaDificultad;
    private final ItemCatalogo accionActivarPista;
    private final ItemCatalogo accionCambiarTipo;

    public RuleBasedAdaptationStrategy(
            List<ItemCatalogo> escalaDificultad,
            ItemCatalogo accionActivarPista,
            ItemCatalogo accionCambiarTipo) {
        this.escalaDificultad = List.copyOf(Objects.requireNonNull(escalaDificultad, "escalaDificultad es obligatoria"));
        this.accionActivarPista = Objects.requireNonNull(accionActivarPista, "accionActivarPista es obligatoria");
        this.accionCambiarTipo = Objects.requireNonNull(accionCambiarTipo, "accionCambiarTipo es obligatoria");
        if (this.escalaDificultad.isEmpty()) {
            throw new IllegalArgumentException("La escala de dificultad no puede estar vacía");
        }
    }

    @Override
    public Optional<AdaptationDecision> decidir(AnalisisRendimiento analisis, PoliticaAdaptacion politica) {
        Objects.requireNonNull(analisis, "analisis es obligatorio");
        Objects.requireNonNull(politica, "politica es obligatoria");
        return politica.reglas().stream()
                .filter(ReglaAdaptacion::activa)
                .sorted(Comparator.comparingInt(ReglaAdaptacion::prioridad))
                .filter(regla -> cumple(regla, analisis))
                .findFirst()
                .map(regla -> construirDecision(regla, analisis));
    }

    private boolean cumple(ReglaAdaptacion regla, AnalisisRendimiento analisis) {
        var contexto = analisis.contexto();
        return cumpleMinimo(contexto.porcentajeAcierto(), regla.porcentajeAciertoMin())
                && cumpleMaximo(contexto.porcentajeAcierto(), regla.porcentajeAciertoMax())
                && cumpleMinimo(contexto.tiempoPromedioMs(), regla.tiempoPromedioMinMs())
                && cumpleMaximo(contexto.tiempoPromedioMs(), regla.tiempoPromedioMaxMs())
                && cumpleMinimo(contexto.rachaAciertos(), regla.rachaAciertosMin())
                && cumpleMinimo(contexto.rachaErrores(), regla.rachaErroresMin());
    }

    private AdaptationDecision construirDecision(ReglaAdaptacion regla, AnalisisRendimiento analisis) {
        var contexto = analisis.contextoAuditable();
        ItemCatalogo dificultadNueva = moverDificultad(contexto.dificultadActual(), regla.accionPrincipal());
        ItemCatalogo tipoNuevo = regla.tipoEjercicioDestino() == null
                ? contexto.tipoEjercicioActual()
                : regla.tipoEjercicioDestino();
        List<AccionEvento> acciones = crearAcciones(regla, contexto, dificultadNueva, tipoNuevo);
        return new AdaptationDecision(
                contexto,
                regla,
                regla.nivelRendimiento(),
                regla.accionPrincipal(),
                contexto.dificultadActual(),
                dificultadNueva,
                contexto.tipoEjercicioActual(),
                tipoNuevo,
                construirMotivo(regla, analisis, dificultadNueva),
                acciones);
    }

    private List<AccionEvento> crearAcciones(
            ReglaAdaptacion regla,
            com.veltia.adaptivequiz.domain.model.ContextoAprendizaje contexto,
            ItemCatalogo dificultadNueva,
            ItemCatalogo tipoNuevo) {
        var acciones = new java.util.ArrayList<AccionEvento>();
        acciones.add(new AccionEvento(
                (short) 1,
                regla.accionPrincipal(),
                "Acción principal de la regla " + regla.codigo(),
                contexto.dificultadActual().codigo(),
                dificultadNueva.codigo(),
                true));
        short secuencia = 2;
        if (regla.habilitarPista()) {
            acciones.add(new AccionEvento(
                    secuencia++,
                    accionActivarPista,
                    "La política habilitó una pista para la siguiente experiencia",
                    "deshabilitada",
                    "habilitada",
                    true));
        }
        if (regla.tipoEjercicioDestino() != null
                && !regla.tipoEjercicioDestino().idItemCatalogo().equals(contexto.tipoEjercicioActual().idItemCatalogo())) {
            acciones.add(new AccionEvento(
                    secuencia,
                    accionCambiarTipo,
                    "La política cambió el tipo de ejercicio",
                    contexto.tipoEjercicioActual().codigo(),
                    tipoNuevo.codigo(),
                    true));
        }
        return List.copyOf(acciones);
    }

    private ItemCatalogo moverDificultad(ItemCatalogo actual, ItemCatalogo accion) {
        int indiceActual = indiceDe(actual);
        int desplazamiento = SUBIR_DIFICULTAD.equals(accion.codigo()) ? 1
                : BAJAR_DIFICULTAD.equals(accion.codigo()) ? -1
                : 0;
        int indiceNuevo = Math.max(0, Math.min(escalaDificultad.size() - 1, indiceActual + desplazamiento));
        return escalaDificultad.get(indiceNuevo);
    }

    private int indiceDe(ItemCatalogo dificultad) {
        for (int indice = 0; indice < escalaDificultad.size(); indice++) {
            if (escalaDificultad.get(indice).idItemCatalogo().equals(dificultad.idItemCatalogo())) {
                return indice;
            }
        }
        throw new IllegalArgumentException("La dificultad actual no pertenece a la escala configurada");
    }

    private String construirMotivo(ReglaAdaptacion regla, AnalisisRendimiento analisis, ItemCatalogo dificultadNueva) {
        StringJoiner condiciones = new StringJoiner("; ");
        if (regla.porcentajeAciertoMin() != null) {
            condiciones.add("precisión " + analisis.contexto().porcentajeAcierto() + " >= " + regla.porcentajeAciertoMin());
        }
        if (regla.porcentajeAciertoMax() != null) {
            condiciones.add("precisión " + analisis.contexto().porcentajeAcierto() + " <= " + regla.porcentajeAciertoMax());
        }
        if (regla.tiempoPromedioMinMs() != null) {
            condiciones.add("tiempo promedio " + analisis.contexto().tiempoPromedioMs() + " ms >= " + regla.tiempoPromedioMinMs());
        }
        if (regla.tiempoPromedioMaxMs() != null) {
            condiciones.add("tiempo promedio " + analisis.contexto().tiempoPromedioMs() + " ms <= " + regla.tiempoPromedioMaxMs());
        }
        if (regla.rachaAciertosMin() != null) {
            condiciones.add("racha de aciertos " + analisis.contexto().rachaAciertos() + " >= " + regla.rachaAciertosMin());
        }
        if (regla.rachaErroresMin() != null) {
            condiciones.add("racha de errores " + analisis.contexto().rachaErrores() + " >= " + regla.rachaErroresMin());
        }
        String evidencia = condiciones.length() == 0
                ? "fallback configurado"
                : condiciones.toString();
        return "Regla " + regla.codigo() + " aplicada: " + evidencia
                + ". Dificultad " + analisis.contexto().dificultadActual().codigo()
                + " -> " + dificultadNueva.codigo() + ".";
    }

    private boolean cumpleMinimo(BigDecimal valor, BigDecimal minimo) {
        return minimo == null || valor.compareTo(minimo) >= 0;
    }

    private boolean cumpleMaximo(BigDecimal valor, BigDecimal maximo) {
        return maximo == null || valor.compareTo(maximo) <= 0;
    }

    private boolean cumpleMinimo(int valor, Integer minimo) {
        return minimo == null || valor >= minimo;
    }

    private boolean cumpleMaximo(int valor, Integer maximo) {
        return maximo == null || valor <= maximo;
    }

    private boolean cumpleMinimo(short valor, Short minimo) {
        return minimo == null || valor >= minimo;
    }
}
