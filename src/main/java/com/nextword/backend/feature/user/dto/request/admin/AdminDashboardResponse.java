package com.nextword.backend.feature.user.dto.request.admin;

public record AdminDashboardResponse(
        Integer activeProfessors,
        Integer classesToday,
        Double monthlyIncome,
        Integer newStudentsThisWeek,
        Integer completedClassesThisWeek,
        Integer cancelledClassesThisWeek,
        Double weeklyIncome
) {}