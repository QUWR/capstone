package org.example.capstone.global.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.capstone.global.exception.CustomException;
import org.example.capstone.global.exception.ErrorCode;
import org.example.capstone.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException() {
        log.error("NullPointer Exception 처리 시작");
        return "NullPointer Exception 핸들링";
    }

    @ExceptionHandler(InternalError.class)
    public String handleInternalError() {
        log.error("InternalError 처리 시작");
        return "InternalError 핸들링";
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException : {}", e.getMessage(), e);

        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
}