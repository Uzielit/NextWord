package com.nextword.backend.feature.user.dto.request;

import java.math.BigDecimal;

public record TopUpRequestDto (
        String studentId,
        BigDecimal amount
){
}
