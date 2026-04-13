package com.nextword.backend.feature.user.dto.request.admin;

public record MonthlyIncomeResponse(
        String month,
        Double amount
) {}