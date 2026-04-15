package com.nextword.backend.feature.reservations.dto;



import jakarta.validation.constraints.NotBlank;

public record CancelClassRequest(
    String reservationId,
   String actionType,
    @NotBlank(message = "Este campo no puede estar blanco ") String reason,
    String requesterId


){
}
