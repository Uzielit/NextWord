package com.nextword.backend.shared.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class Exceptions {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error> handleDuplicateEmail(DataIntegrityViolationException ex) {

        Error error = new Error(
                HttpStatus.CONFLICT.value(), 
                "Conflicto de Datos",
                "El correo o dato ingresado ya se encuentra registrado en el sistema.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGenericError(Exception ex) {

        Error error = new Error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error Interno",
                "Ocurrió un problema inesperado en el servidor.",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
