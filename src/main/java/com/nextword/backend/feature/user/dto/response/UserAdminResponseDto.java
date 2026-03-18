package com.nextword.backend.feature.user.dto.response;

import java.math.BigDecimal;

public record UserAdminResponseDto(
        String id,
        String fullName,
        String email,
        String phoneNumber,
        Integer roleId,
        BigDecimal walletBalance
) {
}