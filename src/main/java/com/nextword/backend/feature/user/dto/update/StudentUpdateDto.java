package com.nextword.backend.feature.user.dto.update;




public record StudentUpdateDto(
        // Datos de la tabla Usuario
        String fullName,
        String phoneNumber,
        String profilePicture,
        String newPassword,

        // Datos de la tabla student
        String tutorName,
        String tutorPhone,
        String tutorEmail

) {
}
