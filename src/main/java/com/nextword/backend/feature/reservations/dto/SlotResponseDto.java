package com.nextword.backend.feature.reservations.dto;

import java.time.LocalDate;

public record SlotResponseDto (
        String slotId,
        String teacherName,
        LocalDate slotDate,
        String startTime,
        String endTime,
        String classType
){
}
