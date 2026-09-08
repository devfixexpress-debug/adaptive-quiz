package com.veltia.adaptivequiz.api.controller;

import com.veltia.adaptivequiz.api.dto.ActualizarPoliticaAdaptativaRequest;
import com.veltia.adaptivequiz.api.dto.ActualizarReglaAdaptativaRequest;
import com.veltia.adaptivequiz.api.dto.ConfiguracionAdaptativaResponse;
import com.veltia.adaptivequiz.api.mappers.ConfiguracionAdaptativaApiMapper;
import com.veltia.adaptivequiz.application.usecase.ActualizarPoliticaAdaptativaCommand;
import com.veltia.adaptivequiz.application.usecase.ActualizarPoliticaAdaptativaUseCase;
import com.veltia.adaptivequiz.application.usecase.ActualizarReglaAdaptativaCommand;
import com.veltia.adaptivequiz.application.usecase.ActualizarReglaAdaptativaUseCase;
import com.veltia.adaptivequiz.application.usecase.ConsultarConfiguracionAdaptativaUseCase;
import com.veltia.adaptivequiz.application.usecase.RestaurarConfiguracionAdaptativaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Uso docente/demostración: mantiene umbrales persistidos sin convertir la dificultad en una
 * preferencia manual del estudiante.
 */
@RestController
@Validated
@RequestMapping("/api/v1/configuracion-adaptativa")
@Tag(name = "Configuración adaptativa", description = "Uso docente: umbrales y ventana de la política activa")
public class ConfiguracionAdaptativaController {

    private final ConsultarConfiguracionAdaptativaUseCase consultarUseCase;
    private final ActualizarReglaAdaptativaUseCase actualizarReglaUseCase;
    private final ActualizarPoliticaAdaptativaUseCase actualizarPoliticaUseCase;
    private final RestaurarConfiguracionAdaptativaUseCase restaurarUseCase;
    private final ConfiguracionAdaptativaApiMapper mapper;

    public ConfiguracionAdaptativaController(
            ConsultarConfiguracionAdaptativaUseCase consultarUseCase,
            ActualizarReglaAdaptativaUseCase actualizarReglaUseCase,
            ActualizarPoliticaAdaptativaUseCase actualizarPoliticaUseCase,
            RestaurarConfiguracionAdaptativaUseCase restaurarUseCase,
            ConfiguracionAdaptativaApiMapper mapper) {
        this.consultarUseCase = consultarUseCase;
        this.actualizarReglaUseCase = actualizarReglaUseCase;
        this.actualizarPoliticaUseCase = actualizarPoliticaUseCase;
        this.restaurarUseCase = restaurarUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Consulta la política activa y los umbrales configurables")
    public ResponseEntity<ConfiguracionAdaptativaResponse> consultar() {
        return ResponseEntity.ok(mapper.toResponse(consultarUseCase.consultarConfiguracionAdaptativa()));
    }

    @PatchMapping("/reglas/{codigoRegla}")
    @Operation(summary = "Actualiza únicamente los umbrales permitidos de una regla existente")
    public ResponseEntity<ConfiguracionAdaptativaResponse> actualizarRegla(
            @PathVariable
            @Pattern(
                    regexp = "R_(ALTO|BAJO_PRECISION|BAJO_RACHA)",
                    message = "sólo se pueden modificar R_ALTO, R_BAJO_PRECISION o R_BAJO_RACHA")
            String codigoRegla,
            @Valid @RequestBody ActualizarReglaAdaptativaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(actualizarReglaUseCase.actualizarReglaAdaptativa(
                new ActualizarReglaAdaptativaCommand(
                        codigoRegla,
                        request.porcentajeAciertoMin(),
                        request.porcentajeAciertoMax(),
                        request.tiempoPromedioMaxMs(),
                        request.rachaErroresMin(),
                        request.habilitarPista()))));
    }

    @PatchMapping("/politica")
    @Operation(summary = "Actualiza la ventana de intentos de la política activa")
    public ResponseEntity<ConfiguracionAdaptativaResponse> actualizarPolitica(
            @Valid @RequestBody ActualizarPoliticaAdaptativaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(actualizarPoliticaUseCase.actualizarPoliticaAdaptativa(
                new ActualizarPoliticaAdaptativaCommand(request.tamanoVentanaIntentos()))));
    }

    @PostMapping("/restaurar-taller")
    @Operation(summary = "Restaura los valores certificados del Taller sin alterar datos de aprendizaje")
    public ResponseEntity<ConfiguracionAdaptativaResponse> restaurarTaller() {
        return ResponseEntity.ok(mapper.toResponse(restaurarUseCase.restaurarValoresTaller()));
    }
}
