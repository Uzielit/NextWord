package com.nextword.backend.feature.user.dto.request.admin;

public record CreateTeacherRequest(
        String fullName,
        String email,
        String password


) {}