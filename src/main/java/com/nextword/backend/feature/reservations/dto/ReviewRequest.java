package com.nextword.backend.feature.reservations.dto;

public record ReviewRequest(
        String reservationId,
        String comment,
        Integer rating



){
}
