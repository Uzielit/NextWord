package com.nextword.backend.feature.user.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record StudentRegistrationRequest (
        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "El formato del correo es inválido")
        String email,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,
        @NotBlank(message = "El nombre completo es obligatorio")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios (sin caracteres especiales)")
        String fullname,
        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener exactamente 10 números")
        String phoneNumber,
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate dateOfBirth,
        @NotBlank(message = "El nombre del tutor es obligatorio")
        String tutorName,
        @NotBlank(message = "El contacto del tutor es obligatorio")
        String tutorContact
){
}
