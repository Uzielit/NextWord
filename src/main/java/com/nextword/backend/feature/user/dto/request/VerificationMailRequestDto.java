package com.nextword.backend.feature.user.dto.request;

public record VerificationMailRequestDto(
        String email,
        String code
) {}