package com.nextword.backend.feature.user.dto.response;

public record TeacherResponseDto(
        String id,
        String fullName,
        String specialization,
        Double averageRating,
        String profetionalDescription,
        String certifications,
        Integer yearsOfExperience
) {
}
