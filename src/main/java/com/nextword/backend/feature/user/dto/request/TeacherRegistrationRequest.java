package com.nextword.backend.feature.user.dto.request;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TeacherRegistrationRequest (
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotBlank String phoneNumber

){
}
