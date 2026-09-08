package com.veltia.adaptivequiz.api.controller;

import com.veltia.adaptivequiz.api.dto.AdaptacionResponse;
import com.veltia.adaptivequiz.api.dto.ProgresoTemaResponse;
import com.veltia.adaptivequiz.api.mappers.PracticaApiMapper;
import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.application.usecase.ConsultarAdaptacionUseCase;
import com.veltia.adaptivequiz.application.usecase.ConsultarProgresoUseCase;
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
@RequestMapping("/api/v1/estudiantes")
@Tag(name = "Progreso", description = "Progreso e historial adaptativo del estudiante")
public class EstudianteController {

    private final ConsultarProgresoUseCase progresoUseCase;
    private final ConsultarAdaptacionUseCase adaptacionUseCase;
    private final PracticaApiMapper mapper;

    public EstudianteController(
            ConsultarProgresoUseCase progresoUseCase,
            ConsultarAdaptacionUseCase adaptacionUseCase,
            PracticaApiMapper mapper) {
        this.progresoUseCase = progresoUseCase;
        this.adaptacionUseCase = adaptacionUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{idEstudiante}/progreso")
    @Operation(summary = "Consulta todos los progresos por tema")
    public ResponseEntity<List<ProgresoTemaResponse>> progreso(
            @PathVariable @Positive Long idEstudiante) {
        return ResponseEntity.ok(progresoUseCase.consultarPorEstudiante(idEstudiante).stream()
                .map(mapper::progreso)
                .toList());
    }

    @GetMapping("/{idEstudiante}/progreso/{idTema}")
    @Operation(summary = "Consulta el progreso de un tema")
    public ResponseEntity<ProgresoTemaResponse> progresoPorTema(
            @PathVariable @Positive Long idEstudiante,
            @PathVariable @Positive Long idTema) {
        return ResponseEntity.ok(progresoUseCase.consultar(idEstudiante, idTema)
                .map(mapper::progreso)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Progreso", "estudiante=" + idEstudiante + ", tema=" + idTema)));
    }

    @GetMapping("/{idEstudiante}/adaptaciones")
    @Operation(summary = "Consulta decisiones adaptativas persistidas")
    public ResponseEntity<List<AdaptacionResponse>> adaptaciones(
            @PathVariable @Positive Long idEstudiante) {
        return ResponseEntity.ok(adaptacionUseCase.consultarAdaptacionesPorEstudiante(idEstudiante).stream()
                .map(mapper::toResponse)
                .toList());
    }
}
