package com.fuyuki.backend.common.exception;

import com.fuyuki.backend.common.api.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResult<?>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(404).body(ApiResult.failed(ex.getMessage()));
    }
}