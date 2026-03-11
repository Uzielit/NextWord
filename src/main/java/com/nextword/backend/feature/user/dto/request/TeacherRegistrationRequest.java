package com.nextword.backend.feature.user.dto.request;

import java.math.BigDecimal;

public record TeacherRegistrationRequest (
        String email,
        String password,
        String fullName,
        String phoneNumber,

    String specialization,
Integer yearsOfExperience,
    String professionalDescription,
    String certifications,
    BigDecimal hourlyRate


){
}
