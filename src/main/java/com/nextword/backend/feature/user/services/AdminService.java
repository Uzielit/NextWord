package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.user.dto.response.UserAdminResponseDto;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserAdminResponseDto> getAllUsers() {
        return userRepository.findAll()
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
    }
}
