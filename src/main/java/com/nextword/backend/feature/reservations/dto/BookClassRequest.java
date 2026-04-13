package com.nextword.backend.feature.reservations.dto;

public record BookClassRequest(
        String studentId,
        String slotId,
        String topic
){
}
