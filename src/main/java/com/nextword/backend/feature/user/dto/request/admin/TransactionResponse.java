package com.nextword.backend.feature.user.dto.request.admin;

public record TransactionResponse(
        String transactionId,
        String topic,
        String studentName,
        String date,
        Double amount
) {}

