package com.nextword.backend.feature.user.dto.request;

public record ClassReportResponse(
        String reservationId,
        String studentName,
        String teacherName,
        String topic,
        String date,
        String status,
        Double amountPaid
) {}
