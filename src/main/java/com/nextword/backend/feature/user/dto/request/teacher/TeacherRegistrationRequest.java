package com.nextword.backend.feature.user.dto.request.teacher;
import jakarta.validation.constraints.*;

public record TeacherRegistrationRequest (
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotBlank String phoneNumber

){
}
