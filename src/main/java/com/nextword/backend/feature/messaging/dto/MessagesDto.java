package com.nextword.backend.feature.messaging.dto;

import jakarta.validation.constraints.NotBlank;

public record MessagesDto(
        @NotBlank(message = "Este campo no puede estar blanco ") String senderId,
        @NotBlank(message = "Este campo no puede estar blanco ") String receiverId,
        @NotBlank(message = "Este campo no puede estar blanco ") String body
){
}
