package com.veltia.adaptivequiz.application.service;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.domain.adaptation.AdaptationEngine;
import com.veltia.adaptivequiz.domain.adaptation.DefaultPerformanceAnalyzer;
import com.veltia.adaptivequiz.domain.adaptation.RuleBasedAdaptationStrategy;
import com.veltia.adaptivequiz.domain.model.ItemCatalogo;
import com.veltia.adaptivequiz.domain.model.ParametroConfiguracion;
import com.veltia.adaptivequiz.domain.repository.CatalogoRepository;
import com.veltia.adaptivequiz.domain.repository.ParametroRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** Ensambla el motor con parámetros y catálogos reales, sin IDs físicos embebidos. */
@Service
public class MotorAdaptativoFactory {

    private static final String CATALOGO_DIFICULTAD = "DIFICULTAD";
    private static final String CATALOGO_ACCION = "ACCION_ADAPTACION";

    private final CatalogoRepository catalogoRepository;
    private final ParametroRepository parametroRepository;

    public MotorAdaptativoFactory(CatalogoRepository catalogoRepository, ParametroRepository parametroRepository) {
        this.catalogoRepository = Objects.requireNonNull(catalogoRepository, "catalogoRepository es obligatorio");
        this.parametroRepository = Objects.requireNonNull(parametroRepository, "parametroRepository es obligatorio");
    }

    public AdaptationEngine crearMotor() {
        List<ItemCatalogo> dificultades = catalogoRepository.findItemsActivos(CATALOGO_DIFICULTAD);
        ItemCatalogo activarPista = item(CATALOGO_ACCION, "ACTIVAR_PISTA");
        ItemCatalogo cambiarTipo = item(CATALOGO_ACCION, "CAMBIAR_TIPO_EJERCICIO");
        DefaultPerformanceAnalyzer analyzer = new DefaultPerformanceAnalyzer(
                decimal("PESO_PRECISION"),
                decimal("PESO_VELOCIDAD"),
                decimal("PESO_CONSISTENCIA"),
                entero("TIEMPO_RAPIDO_MS"));
        return new AdaptationEngine(
                analyzer,
                new RuleBasedAdaptationStrategy(dificultades, activarPista, cambiarTipo));
    }

    private ItemCatalogo item(String codigoCatalogo, String codigoItem) {
        return catalogoRepository.findItemActivo(codigoCatalogo, codigoItem)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ítem de catálogo", codigoCatalogo + "/" + codigoItem));
    }

    private BigDecimal decimal(String codigo) {
        ParametroConfiguracion parametro = parametro(codigo);
        if (parametro.valorDecimal() == null) {
            throw new IllegalStateException("El parámetro " + codigo + " debe tener valor decimal");
        }
        return parametro.valorDecimal();
    }

    private int entero(String codigo) {
        ParametroConfiguracion parametro = parametro(codigo);
        if (parametro.valorEntero() == null || parametro.valorEntero() > Integer.MAX_VALUE) {
            throw new IllegalStateException("El parámetro " + codigo + " debe tener valor entero válido");
        }
        return parametro.valorEntero().intValue();
    }

    private ParametroConfiguracion parametro(String codigo) {
        return parametroRepository.findActivoByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Parámetro", codigo));
    }
}
