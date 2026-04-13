package com.nextword.backend.feature.user.dto.request;

public record MonthlyIncomeResponse(
        String month,
        Double amount
) {}