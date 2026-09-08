package com.veltia.adaptivequiz.api.controller;

import com.veltia.adaptivequiz.api.dto.IniciarSesionPracticaRequest;
import com.veltia.adaptivequiz.api.dto.RegistrarIntentoRequest;
import com.veltia.adaptivequiz.api.dto.ResultadoIntentoResponse;
import com.veltia.adaptivequiz.api.dto.SesionPracticaResponse;
import com.veltia.adaptivequiz.api.mappers.PracticaApiMapper;
import com.veltia.adaptivequiz.application.usecase.IniciarSesionPracticaCommand;
import com.veltia.adaptivequiz.application.usecase.IniciarSesionPracticaUseCase;
import com.veltia.adaptivequiz.application.usecase.RegistrarIntentoCommand;
import com.veltia.adaptivequiz.application.usecase.RegistrarIntentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Práctica", description = "Sesiones, intentos y procesamiento adaptativo")
public class PracticaController {

    private final IniciarSesionPracticaUseCase iniciarSesionUseCase;
    private final RegistrarIntentoUseCase registrarIntentoUseCase;
    private final PracticaApiMapper mapper;

    public PracticaController(
            IniciarSesionPracticaUseCase iniciarSesionUseCase,
            RegistrarIntentoUseCase registrarIntentoUseCase,
            PracticaApiMapper mapper) {
        this.iniciarSesionUseCase = iniciarSesionUseCase;
        this.registrarIntentoUseCase = registrarIntentoUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/sesiones-practica")
    @Operation(summary = "Inicia una sesión con política y dificultad inicial parametrizadas")
    public ResponseEntity<SesionPracticaResponse> iniciar(@Valid @RequestBody IniciarSesionPracticaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(iniciarSesionUseCase.iniciar(
                new IniciarSesionPracticaCommand(request.estudianteId(), request.temaId()))));
    }

    @PostMapping("/intentos")
    @Operation(summary = "Registra, califica y adapta una resolución real")
    public ResponseEntity<ResultadoIntentoResponse> registrar(@Valid @RequestBody RegistrarIntentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(registrarIntentoUseCase.registrar(
                new RegistrarIntentoCommand(
                        request.sesionPracticaId(),
                        request.ejercicioId(),
                        request.opcionesSeleccionadas(),
                        request.tiempoRespuestaMs(),
                        Boolean.TRUE.equals(request.usoPista())))));
    }
}
