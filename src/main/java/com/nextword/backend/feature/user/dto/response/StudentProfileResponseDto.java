package com.nextword.backend.feature.user.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;


public record StudentProfileResponseDto(
        String id,
        String fullName,
        String email,
        BigDecimal walletBalance,
        LocalDate dateOfBirth,
        String tutorName,
        String tutorEmail,
        String tutorPhone
) {

}
