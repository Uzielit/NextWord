package com.nextword.backend.feature.reservations.dto;

import java.time.LocalDate;

public record ReservationResponseDto (
        String reservationId,
        String participantName,
        LocalDate date,
        String startTime,
        String endTime,
        String classType,
        String status,
        String meetLink
){
}
