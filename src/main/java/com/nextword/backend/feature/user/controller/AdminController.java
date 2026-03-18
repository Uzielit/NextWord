package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.response.UserAdminResponseDto;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserAdminResponseDto>> getAllUsers() {

        List<UserAdminResponseDto> cleanUsers = userRepository.findAll()
                .stream()
                .map(user -> new UserAdminResponseDto(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRoleId(),
                        user.getWalletBalance()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(cleanUsers);
    }


}
