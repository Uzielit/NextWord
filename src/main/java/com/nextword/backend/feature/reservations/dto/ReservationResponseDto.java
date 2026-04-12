package com.nextword.backend.feature.reservations.dto;

import java.time.LocalDate;

public record ReservationResponseDto (
        String reservationId,
        String studentName,
        String teacherName,
        LocalDate date,
        String startTime,
        String endTime,
        String topic,
        String status,
        String meetLink,
        Boolean hasReview
){
}
