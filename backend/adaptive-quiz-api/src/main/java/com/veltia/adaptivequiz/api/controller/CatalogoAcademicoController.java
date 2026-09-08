package com.veltia.adaptivequiz.api.controller;

import com.veltia.adaptivequiz.api.dto.AsignaturaResponse;
import com.veltia.adaptivequiz.api.dto.TemaResponse;
import com.veltia.adaptivequiz.api.mappers.CatalogoAcademicoApiMapper;
import com.veltia.adaptivequiz.application.usecase.ConsultarAsignaturasUseCase;
import com.veltia.adaptivequiz.application.usecase.ConsultarTemasUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/asignaturas")
@Tag(name = "Catálogo académico", description = "Consultas disponibles para asignaturas y temas")
public class CatalogoAcademicoController {

    private final ConsultarAsignaturasUseCase consultarAsignaturasUseCase;
    private final ConsultarTemasUseCase consultarTemasUseCase;
    private final CatalogoAcademicoApiMapper mapper;

    public CatalogoAcademicoController(
            ConsultarAsignaturasUseCase consultarAsignaturasUseCase,
            ConsultarTemasUseCase consultarTemasUseCase,
            CatalogoAcademicoApiMapper mapper) {
        this.consultarAsignaturasUseCase = consultarAsignaturasUseCase;
        this.consultarTemasUseCase = consultarTemasUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    @Operation(summary = "Lista las asignaturas académicas activas")
    public ResponseEntity<List<AsignaturaResponse>> listarAsignaturas() {
        return ResponseEntity.ok(consultarAsignaturasUseCase.consultarAsignaturas().stream()
                .map(mapper::toResponse)
                .toList());
    }

    @GetMapping("/{idAsignatura}/temas")
    @Operation(summary = "Lista los temas activos de una asignatura")
    public ResponseEntity<List<TemaResponse>> listarTemas(
            @PathVariable @Positive(message = "idAsignatura debe ser positivo") Long idAsignatura) {
        return ResponseEntity.ok(consultarTemasUseCase.consultarTemasDeAsignatura(idAsignatura).stream()
                .map(mapper::toResponse)
                .toList());
    }
}
