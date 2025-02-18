package com.hiarc.Hiting.global.common.exception.handler;

import com.hiarc.Hiting.global.common.apiPayload.ApiResponse;
import com.hiarc.Hiting.global.common.apiPayload.code.status.ErrorStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidDateFormat(HttpMessageNotReadableException e) {

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.of(ErrorStatus.INVALID_DATE_FORMAT, null));
    }
}
