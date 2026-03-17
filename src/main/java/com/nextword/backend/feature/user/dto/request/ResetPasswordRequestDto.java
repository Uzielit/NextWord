package com.nextword.backend.feature.user.dto.request;

public record ResetPasswordRequestDto(
        String token,
        String newPassword
) {
}