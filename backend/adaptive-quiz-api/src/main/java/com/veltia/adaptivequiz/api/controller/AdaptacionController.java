package com.veltia.adaptivequiz.api.controller;

import com.veltia.adaptivequiz.api.dto.AdaptacionResponse;
import com.veltia.adaptivequiz.api.mappers.PracticaApiMapper;
import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import com.veltia.adaptivequiz.application.usecase.ConsultarAdaptacionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/adaptaciones")
@Tag(name = "Adaptación", description = "Detalle explicable de decisiones del motor")
public class AdaptacionController {

    private final ConsultarAdaptacionUseCase useCase;
    private final PracticaApiMapper mapper;

    public AdaptacionController(ConsultarAdaptacionUseCase useCase, PracticaApiMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @GetMapping("/{idAdaptacion}")
    @Operation(summary = "Obtiene el contexto, decisión y acciones de una adaptación")
    public ResponseEntity<AdaptacionResponse> obtener(@PathVariable @Positive Long idAdaptacion) {
        return ResponseEntity.ok(useCase.consultarPorId(idAdaptacion)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Adaptación", idAdaptacion)));
    }
}
