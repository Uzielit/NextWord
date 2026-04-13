package com.nextword.backend.feature.reservations.dto;



public record CancelClassRequest(
    String reservationId,
    String actionType,
    String reason,
    String requesterId


){
}
