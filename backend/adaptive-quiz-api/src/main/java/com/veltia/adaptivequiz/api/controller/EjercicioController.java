package com.veltia.adaptivequiz.api.controller;

import com.veltia.adaptivequiz.api.dto.EjercicioResponse;
import com.veltia.adaptivequiz.api.mappers.EjercicioApiMapper;
import com.veltia.adaptivequiz.application.usecase.ObtenerEjercicioUseCase;
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
@RequestMapping("/api/v1/ejercicios")
@Tag(name = "Ejercicios", description = "Consulta del banco académico")
public class EjercicioController {

    private final ObtenerEjercicioUseCase obtenerEjercicioUseCase;
    private final EjercicioApiMapper mapper;

    public EjercicioController(ObtenerEjercicioUseCase obtenerEjercicioUseCase, EjercicioApiMapper mapper) {
        this.obtenerEjercicioUseCase = obtenerEjercicioUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{idEjercicio}")
    @Operation(summary = "Obtiene el detalle visible de un ejercicio")
    public ResponseEntity<EjercicioResponse> obtenerEjercicio(
            @PathVariable @Positive(message = "idEjercicio debe ser positivo") Long idEjercicio) {
        return ResponseEntity.ok(mapper.toResponse(obtenerEjercicioUseCase.obtenerEjercicio(idEjercicio)));
    }
}
