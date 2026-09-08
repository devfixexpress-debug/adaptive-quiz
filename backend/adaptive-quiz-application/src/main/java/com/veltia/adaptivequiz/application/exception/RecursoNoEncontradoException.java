package com.veltia.adaptivequiz.application.exception;

/** Error de aplicación traducido por la capa REST a HTTP 404. */
public final class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " no fue encontrado para id=" + id);
    }

    public RecursoNoEncontradoException(String recurso, String criterio) {
        super(recurso + " no fue encontrado para " + criterio);
    }
}
