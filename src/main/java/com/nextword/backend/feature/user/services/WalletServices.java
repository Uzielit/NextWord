package com.nextword.backend.feature.user.services;

import com.nextword.backend.feature.user.dto.request.TopUpRequestDto;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServices {
    private final UserRepository userRepository;

    public WalletServices(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public String addFunds(TopUpRequestDto request) {
        User student = userRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        BigDecimal currentBalance = student.getWalletBalance() != null ? student.getWalletBalance() : BigDecimal.ZERO;


        BigDecimal newBalance = currentBalance.add(request.amount());
        student.setWalletBalance(newBalance);

        userRepository.save(student);

        return "Recarga exitosa. El nuevo saldo del alumno es: $" + newBalance;
    }

}
