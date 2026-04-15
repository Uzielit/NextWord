package com.nextword.backend.feature.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerificationMailRequestDto(
        @NotBlank(message = "Este campo no puede estar blanco ") @Email(message = "Este campo debe ser un email") String email,
        @NotBlank(message = "Este campo no puede estar blanco ") String code
) {}