package com.veltia.adaptivequiz.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/** Definición reutilizable de contenido; no representa un intento real. */
public record Ejercicio(
        Long idEjercicio,
        Long idTema,
        String codigo,
        String enunciado,
        ItemCatalogo tipoEjercicio,
        ItemCatalogo dificultad,
        ItemCatalogo estado,
        String explicacion,
        Integer tiempoObjetivoSegundos,
        BigDecimal puntajeBase,
        List<OpcionEjercicio> opciones,
        List<PistaEjercicio> pistas
) {
    public Ejercicio {
        opciones = List.copyOf(Objects.requireNonNull(opciones, "opciones es obligatorio"));
        pistas = List.copyOf(Objects.requireNonNull(pistas, "pistas es obligatorio"));
    }

    /**
     * Invariante aplicable por el caso de uso que maneje ejercicios de opción única.
     * El tipo se mantiene paramétrico en CAT_ITEM_CATALOGO, por eso esta entidad no lo fija con un enum.
     */
    public void validarUnaOpcionCorrecta() {
        long correctas = opciones.stream().filter(OpcionEjercicio::esCorrecta).count();
        if (correctas != 1) {
            throw new IllegalStateException("Un ejercicio de opción única debe tener exactamente una opción correcta");
        }
    }
}
