package com.fuyuki.backend.common.exception;

import com.fuyuki.backend.common.api.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResult<?>> handleRuntimeException(RuntimeException ex) {
        if (ex.getMessage().contains("Token expired")) {
            // 处理 Token 过期的情况
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResult.unauthorized(null));
        } else {
            // 处理其他运行时异常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResult.failed(ex.getMessage()));
        }
    }
}