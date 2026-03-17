package com.nextword.backend.feature.user.dto;

public record LoginRequestDto(
        String email,
        String password
) {
}
