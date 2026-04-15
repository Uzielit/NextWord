package com.nextword.backend.feature.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TopUpRequestDto (
        @NotBlank(message = "Este campo no puede estar blanco ") String studentId,
        @NotNull(message = "Este campo no puede ser nulo ") @Positive(message = "El monto debe ser positivo") BigDecimal amount
){
}
