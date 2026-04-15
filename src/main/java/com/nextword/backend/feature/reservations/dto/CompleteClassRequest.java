package com.nextword.backend.feature.reservations.dto;

import jakarta.validation.constraints.NotBlank;

public record CompleteClassRequest (
       String reservationId,
      String studentAttendance,
       String teacherAttendance
){
}
