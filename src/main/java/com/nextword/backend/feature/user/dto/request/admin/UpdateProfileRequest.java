package com.nextword.backend.feature.user.dto.request.admin;

public record UpdateProfileRequest(
        String fullName,
        String phoneNumber,
        String profilePicture
) {}