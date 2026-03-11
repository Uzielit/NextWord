package com.nextword.backend.feature.user.dto.request;

import java.time.LocalDate;

public record StudentRegistrationRequest (
        String email,
        String password,
        String fullName,
        String phoneNumber,
        LocalDate dateOfBirth,
        String tutorName,
        String tutorContact
){
}
