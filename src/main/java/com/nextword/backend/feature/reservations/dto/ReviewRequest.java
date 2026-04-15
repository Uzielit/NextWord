package com.nextword.backend.feature.reservations.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
        @NotBlank(message = "Este campo no puede estar blanco ") String reservationId,
        String comment,
        @NotNull(message = "Este campo no puede ser nulo ") @Min(1) @Max(5) Integer rating



){
}
