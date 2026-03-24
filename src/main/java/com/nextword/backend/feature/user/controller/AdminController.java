package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.response.UserAdminResponseDto;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import com.nextword.backend.feature.user.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserAdminResponseDto>> getAllUsers() {

        return ResponseEntity.ok(adminService.getAllUsers());
    }


}
