package com.nextword.backend.feature.reservations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateSlotRequest(
     String teacherId,
        LocalDate slotDate,
        @NotBlank(message = "Este campo no puede estar blanco ") String startTime,
        @NotBlank(message = "Este campo no puede estar blanco ") String endTime,
       String classType
){
}
