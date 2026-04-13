package com.nextword.backend.feature.reservations.dto;

import java.time.LocalDate;

public record CreateSlotRequest(
        String teacherId,
        LocalDate slotDate,
        String startTime,
        String endTime,
        String classType
){
}
