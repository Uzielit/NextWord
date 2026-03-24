package com.nextword.backend.feature.user.dto.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TeacherProfileUpdateDto(
        @NotBlank String specialization,
        @NotNull @Min(0) Integer yearsOfExperience,
        @NotBlank @Size(max = 2000) String professionalDescription,
        @NotBlank String certifications
) {}
