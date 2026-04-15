package com.nextword.backend.feature.user.dto.request.admin;

import java.util.List;

public record FinancialReportResponse(
        List<MonthlyIncomeResponse> chartData,
        List<TransactionResponse> recentTransactions
) {}