package com.nextword.backend.feature.reservations.dto;

import jakarta.validation.constraints.NotBlank;

public record BookClassRequest(
        String studentId,
        String slotId,
       String topic
){
}
