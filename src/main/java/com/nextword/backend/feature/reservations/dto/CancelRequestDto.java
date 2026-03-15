package com.nextword.backend.feature.reservations.dto;



public record CancelRequestDto (

    String reservaId,
    String actionType,
    String reason,
    String requesterId


){
}
