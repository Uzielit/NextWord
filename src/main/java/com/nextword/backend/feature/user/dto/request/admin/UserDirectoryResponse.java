package com.nextword.backend.feature.user.dto.request.admin;

import java.time.ZonedDateTime;

public record UserDirectoryResponse(
        String userId,
        String fullName,
        String email,
        Integer roleId,
        String status,
        ZonedDateTime registrationDate
) {}