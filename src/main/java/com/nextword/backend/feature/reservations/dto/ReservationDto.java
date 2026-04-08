package com.nextword.backend.feature.reservations.dto;

public record ReservationDto (
        String studentId,
        String slotId,
        String topic
){
}
