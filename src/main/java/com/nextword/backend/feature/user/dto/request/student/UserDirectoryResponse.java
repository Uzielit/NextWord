package com.nextword.backend.feature.user.dto.request.student;

import java.time.ZonedDateTime;

public record UserDirectoryResponse(
        String userId,
        String fullName,
        String email,
        String role,
        ZonedDateTime registrationDate,
        String status
) {}