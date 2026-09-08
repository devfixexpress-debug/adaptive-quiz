package com.veltia.adaptivequiz.api.exception;

import com.veltia.adaptivequiz.application.exception.RecursoNoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Contrato uniforme de errores, reutilizado del patrón VAEF sin exponer detalles internos. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(RecursoNoEncontradoException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "RECURSO_NO_ENCONTRADO", exception.getMessage(), request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleValidation(Exception exception, HttpServletRequest request) {
        String message = exception instanceof MethodArgumentNotValidException validationException
                ? validationException.getBindingResult().getFieldErrors().stream()
                        .findFirst()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .orElse("Solicitud inválida")
                : exception.getMessage();
        return error(HttpStatus.BAD_REQUEST, "VALIDACION", message, request);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception exception, HttpServletRequest request) {
        String message = exception instanceof HttpMessageNotReadableException
                ? "Solicitud JSON inválida"
                : exception.getMessage();
        return error(HttpStatus.BAD_REQUEST, "SOLICITUD_INVALIDA", message, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        LOGGER.warn("Violación de integridad en {} {}", request.getMethod(), request.getRequestURI(), exception);
        return error(HttpStatus.CONFLICT, "INTEGRIDAD_DATOS", "Violación de integridad de datos", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        LOGGER.error("Error no controlado en {} {}", request.getMethod(), request.getRequestURI(), exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", "Error interno inesperado", request);
    }

    private ResponseEntity<ApiErrorResponse> error(
            HttpStatus status,
            String codigo,
            String message,
            HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.name(),
                codigo,
                message,
                request.getRequestURI()));
    }
}
