package com.veltia.adaptivequiz.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.veltia.adaptivequiz.domain.model.Intento;
import com.veltia.adaptivequiz.domain.model.IntentoContextual;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ProgresoTema;
import com.veltia.adaptivequiz.domain.repository.IntentoRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class LearningContextBuilderTest {

    @Test
    void construye_ventana_con_precision_tiempo_y_racha_reciente() {
        ItemCatalogo correcto = item(1, "CORRECTO");
        ItemCatalogo dificultad = item(2, "INTERMEDIO");
        ItemCatalogo tipo = item(3, "OPCION_UNICA");
        List<IntentoContextual> recientes = List.of(
                contextual(5L, false, 30000, dificultad, tipo),
                contextual(4L, false, 20000, dificultad, tipo),
                contextual(3L, false, 10000, dificultad, tipo),
                contextual(2L, true, 10000, dificultad, tipo),
                contextual(1L, true, 5000, dificultad, tipo));
        LearningContextBuilder builder = new LearningContextBuilder(new IntentoRepository() {
            @Override
            public Intento guardar(Intento intento) {
                return intento;
            }

            @Override
            public List<IntentoContextual> findRecientesParaContexto(Long estudiante, Long tema, int limite) {
                return recientes;
            }

            @Override
            public List<Long> findIdsEjerciciosResueltosPorSesion(Long sesion) {
                return List.of();
            }
        });

        var contexto = builder.construir(
                new Intento(5L, 1L, 5L, 5, OffsetDateTime.now(), OffsetDateTime.now(), 30000,
                        correcto, BigDecimal.ZERO, false),
                new PoliticaAdaptacion(1L, "POL", "Política", 1, item(4, "REGLAS"), (short) 5,
                        OffsetDateTime.now(), null, true, List.of()),
                new ProgresoTema(1L, 1L, 1L, dificultad, 5, 2, new BigDecimal("0.4000"), 15000,
                        0, 3, OffsetDateTime.now()));

        assertEquals((short) 5, contexto.numeroIntentosVentana());
        assertEquals((short) 2, contexto.totalAciertosVentana());
        assertEquals(new BigDecimal("0.4000"), contexto.porcentajeAcierto());
        assertEquals(15000, contexto.tiempoPromedioMs());
        assertEquals((short) 0, contexto.rachaAciertos());
        assertEquals((short) 3, contexto.rachaErrores());
    }

    private IntentoContextual contextual(
            Long id,
            boolean correcto,
            int tiempo,
            ItemCatalogo dificultad,
            ItemCatalogo tipo) {
        return new IntentoContextual(id, correcto, tiempo, dificultad, tipo, OffsetDateTime.now());
    }

    private ItemCatalogo item(long id, String codigo) {
        return new ItemCatalogo(id, codigo, codigo);
    }
}
