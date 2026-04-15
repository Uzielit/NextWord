package com.nextword.backend.feature.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "Este campo no puede estar blanco ") @Email(message = "Este campo debe ser un email") String email,
        @NotBlank(message = "Este campo no puede estar blanco ") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password
) {
}
