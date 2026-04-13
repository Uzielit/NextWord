package com.nextword.backend.feature.payments.controller;

import com.nextword.backend.feature.payments.services.PaymentService;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;

    public PaymentController(PaymentService paymentService, UserRepository userRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @PostMapping("/claim-payment")
    public ResponseEntity<?> claimPayment(Principal principal, @RequestParam Long paymentId) {
        try {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no identificado"));
            String mensaje = paymentService.claimStaticLinkPayment(user.getId(), paymentId);
            return ResponseEntity.ok(Map.of("message", mensaje));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}