package com.nextword.backend.feature.user.dto.request;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TeacherRegistrationRequest (
        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "El formato del correo es inválido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
        String fullName,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener exactamente 10 números")
        String phoneNumber,

        @NotBlank(message = "La especialización es obligatoria")
        String specialization,

        @NotNull(message = "Los años de experiencia son obligatorios")
        @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
        Integer yearsOfExperience,

        @NotBlank(message = "La descripción profesional es obligatoria")
        @Size(max = 2000, message = "La descripción no puede exceder los 2000 caracteres")
        String professionalDescription,

        @NotBlank(message = "Las certificaciones son obligatorias")
        String certifications,

        @NotNull(message = "La tarifa por hora es obligatoria")
        @DecimalMin(value = "0.0", inclusive = false, message = "La tarifa por hora debe ser mayor a 0")
        BigDecimal hourlyRate


){
}
