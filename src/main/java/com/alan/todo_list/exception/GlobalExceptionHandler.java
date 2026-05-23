package com.alan.todo_list.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse error =ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .mensagem(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .mensagem("Status inválido. Valores aceitos: PENDING, COMPLETED, CANCELED")
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .mensagem(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
