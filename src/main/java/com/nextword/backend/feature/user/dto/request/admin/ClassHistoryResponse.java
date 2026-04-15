package com.nextword.backend.feature.user.dto.request.admin;

public record ClassHistoryResponse(
        String reservationId,
        String topic,
        String studentName,
        String teacherName,
        String datetime,
        String status
) {}