package com.nextword.backend.shared.exception;

import java.time.LocalDateTime;

public record Error (
        int status,
        String error,
        String message,
        LocalDateTime timestamp
){
}
