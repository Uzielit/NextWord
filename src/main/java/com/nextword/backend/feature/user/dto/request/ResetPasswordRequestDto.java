package com.nextword.backend.feature.user.dto.request;

public record ResetPasswordRequestDto(
        String email,
        String token,
        String newPassword
){

}