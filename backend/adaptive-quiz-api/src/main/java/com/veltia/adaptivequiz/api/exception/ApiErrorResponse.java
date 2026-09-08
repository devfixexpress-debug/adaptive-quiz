package com.veltia.adaptivequiz.api.exception;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String codigo,
        String message,
        String path
) {
}
