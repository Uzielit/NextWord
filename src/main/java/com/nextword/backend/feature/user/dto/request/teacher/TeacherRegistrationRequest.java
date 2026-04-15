package com.nextword.backend.feature.user.dto.request.teacher;
import jakarta.validation.constraints.*;

public record TeacherRegistrationRequest (
        @NotBlank(message = "Este campo no puede estar blanco ") @Email(message = "Este campo debe ser un email") String email,
        @NotBlank(message = "Este campo no puede estar blanco ") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password,
        @NotBlank(message = "Este campo no puede estar blanco ") String fullName,
        @NotBlank(message = "Este campo no puede estar blanco ") String phoneNumber

){
}
