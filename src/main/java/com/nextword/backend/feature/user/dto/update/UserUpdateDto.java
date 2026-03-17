package com.nextword.backend.feature.user.dto.update;



public record UserUpdateDto(
        String fullName,
        String phoneNumber,
        String profilePicture,
        String newPassword

) {
}