package com.veltia.adaptivequiz.domain.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class EjercicioTest {

    @Test
    void valida_una_sola_opcion_correcta() {
        Ejercicio ejercicio = ejercicioCon(List.of(
                new OpcionEjercicio(1L, "A", "2", false, 1, null),
                new OpcionEjercicio(2L, "B", "4", true, 2, null)));

        assertDoesNotThrow(ejercicio::validarUnaOpcionCorrecta);
    }

    @Test
    void rechaza_cero_o_multiples_opciones_correctas() {
        Ejercicio sinCorrecta = ejercicioCon(List.of(
                new OpcionEjercicio(1L, "A", "2", false, 1, null)));
        Ejercicio dosCorrectas = ejercicioCon(List.of(
                new OpcionEjercicio(1L, "A", "2", true, 1, null),
                new OpcionEjercicio(2L, "B", "4", true, 2, null)));

        assertThrows(IllegalStateException.class, sinCorrecta::validarUnaOpcionCorrecta);
        assertThrows(IllegalStateException.class, dosCorrectas::validarUnaOpcionCorrecta);
    }

    private Ejercicio ejercicioCon(List<OpcionEjercicio> opciones) {
        ItemCatalogo tipo = new ItemCatalogo(1L, "OPCION_UNICA", "Opción única");
        ItemCatalogo dificultad = new ItemCatalogo(2L, "BASICO", "Básico");
        ItemCatalogo estado = new ItemCatalogo(3L, "PUBLICADO", "Publicado");
        return new Ejercicio(1L, 1L, "ALG-B-001", "2x + 4 = 12", tipo, dificultad, estado,
                null, 20, BigDecimal.ONE, opciones, List.of());
    }
}
