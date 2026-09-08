package com.veltia.adaptivequiz.domain.adaptation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.veltia.adaptivequiz.domain.model.ContextoAprendizaje;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.PoliticaAdaptacion;
import com.veltia.adaptivequiz.domain.model.ReglaAdaptacion;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class RuleBasedAdaptationStrategyTest {

    private final ItemCatalogo basico = item(1, "BASICO");
    private final ItemCatalogo intermedio = item(2, "INTERMEDIO");
    private final ItemCatalogo avanzado = item(3, "AVANZADO");
    private final ItemCatalogo bajo = item(4, "BAJO");
    private final ItemCatalogo medio = item(5, "MEDIO");
    private final ItemCatalogo alto = item(6, "ALTO");
    private final ItemCatalogo subir = item(7, "SUBIR_DIFICULTAD");
    private final ItemCatalogo mantener = item(8, "MANTENER_DIFICULTAD");
    private final ItemCatalogo bajar = item(9, "BAJAR_DIFICULTAD");
    private final ItemCatalogo pista = item(10, "ACTIVAR_PISTA");
    private final ItemCatalogo cambiarTipo = item(11, "CAMBIAR_TIPO_EJERCICIO");
    private final ItemCatalogo opcionUnica = item(12, "OPCION_UNICA");

    @Test
    void aplica_bajo_por_racha_antes_que_precision_y_habilita_pista() {
        var decision = estrategia().decidir(analisis(contexto(intermedio, "0.20", 30000, 0, 3)), politica(
                regla("R_BAJO_RACHA", 10, null, null, null, null, null, (short) 3, bajo, bajar, true),
                regla("R_BAJO_PRECISION", 20, null, bd("0.40"), null, null, null, null, bajo, bajar, true),
                fallback())).orElseThrow();

        assertEquals("R_BAJO_RACHA", decision.regla().codigo());
        assertEquals("BASICO", decision.dificultadNueva().codigo());
        assertTrue(decision.acciones().stream().anyMatch(a -> "ACTIVAR_PISTA".equals(a.accionAdaptacion().codigo())));
    }

    @Test
    void aplica_bajo_por_precision() {
        var decision = estrategia().decidir(analisis(contexto(intermedio, "0.40", 25000, 0, 1)), politica(
                regla("R_BAJO_PRECISION", 20, null, bd("0.40"), null, null, null, null, bajo, bajar, true),
                fallback())).orElseThrow();

        assertEquals("R_BAJO_PRECISION", decision.regla().codigo());
        assertEquals("BASICO", decision.dificultadNueva().codigo());
    }

    @Test
    void aplica_alto_con_precision_y_tiempo() {
        var decision = estrategia().decidir(analisis(contexto(intermedio, "0.80", 20000, 3, 0)), politica(
                regla("R_ALTO", 30, bd("0.80"), null, null, 20000, null, null, alto, subir, false),
                fallback())).orElseThrow();

        assertEquals("R_ALTO", decision.regla().codigo());
        assertEquals("AVANZADO", decision.dificultadNueva().codigo());
    }

    @Test
    void usa_fallback_medio_cuando_no_hay_otra_regla() {
        var decision = estrategia().decidir(analisis(contexto(intermedio, "0.60", 24000, 1, 0)), politica(
                regla("R_ALTO", 30, bd("0.80"), null, null, 20000, null, null, alto, subir, false),
                fallback())).orElseThrow();

        assertEquals("R_MEDIO", decision.regla().codigo());
        assertEquals("MEDIO", decision.nivelRendimiento().codigo());
        assertEquals("INTERMEDIO", decision.dificultadNueva().codigo());
    }

    @Test
    void no_baja_de_basico() {
        var decision = estrategia().decidir(analisis(contexto(basico, "0.10", 40000, 0, 3)), politica(
                regla("R_BAJO_RACHA", 10, null, null, null, null, null, (short) 3, bajo, bajar, true))).orElseThrow();

        assertEquals("BASICO", decision.dificultadNueva().codigo());
    }

    @Test
    void no_sube_de_avanzado() {
        var decision = estrategia().decidir(analisis(contexto(avanzado, "1.00", 10000, 4, 0)), politica(
                regla("R_ALTO", 30, bd("0.80"), null, null, 20000, null, null, alto, subir, false))).orElseThrow();

        assertEquals("AVANZADO", decision.dificultadNueva().codigo());
    }

    @Test
    void calcula_puntaje_rendimiento_acotado() {
        AnalisisRendimiento analisis = analisis(contexto(intermedio, "1.00", 5000, 5, 0));

        assertEquals(new BigDecimal("0.937500"), analisis.puntajeRendimiento());
        assertEquals(new BigDecimal("0.937500"), analisis.contextoAuditable().puntajeRendimiento());
    }

    private RuleBasedAdaptationStrategy estrategia() {
        return new RuleBasedAdaptationStrategy(List.of(basico, intermedio, avanzado), pista, cambiarTipo);
    }

    private AnalisisRendimiento analisis(ContextoAprendizaje contexto) {
        return new DefaultPerformanceAnalyzer(bd("0.60"), bd("0.25"), bd("0.15"), 20000).analizar(contexto);
    }

    private PoliticaAdaptacion politica(ReglaAdaptacion... reglas) {
        return new PoliticaAdaptacion(1L, "POLITICA", "Política", 1, item(20, "REGLAS"), (short) 5,
                OffsetDateTime.now(), null, true, List.of(reglas));
    }

    private ReglaAdaptacion regla(
            String codigo,
            int prioridad,
            BigDecimal precisionMin,
            BigDecimal precisionMax,
            Integer tiempoMin,
            Integer tiempoMax,
            Short rachaAciertos,
            Short rachaErrores,
            ItemCatalogo rendimiento,
            ItemCatalogo accion,
            boolean habilitarPista) {
        return new ReglaAdaptacion((long) prioridad, codigo, codigo, prioridad, precisionMin, precisionMax,
                tiempoMin, tiempoMax, rachaAciertos, rachaErrores, rendimiento, accion, null, habilitarPista, true);
    }

    private ReglaAdaptacion fallback() {
        return regla("R_MEDIO", 90, null, null, null, null, null, null, medio, mantener, false);
    }

    private ContextoAprendizaje contexto(
            ItemCatalogo dificultad,
            String precision,
            int tiempoPromedio,
            int rachaAciertos,
            int rachaErrores) {
        return new ContextoAprendizaje(null, 1L, 1L, 1L, 1L, (short) 5, (short) 3, bd(precision),
                tiempoPromedio, (short) rachaAciertos, (short) rachaErrores, dificultad, opcionUnica, null);
    }

    private ItemCatalogo item(long id, String codigo) {
        return new ItemCatalogo(id, codigo, codigo);
    }

    private BigDecimal bd(String value) {
        return new BigDecimal(value);
    }
}
