package com.nextword.backend.feature.reservations.dto;

public record CompleteClassRequest (
        String reservationId,
        String studentAttendance,
        String teacherAttendance
){
}
