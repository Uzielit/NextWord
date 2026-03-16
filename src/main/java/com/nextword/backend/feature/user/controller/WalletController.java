package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.request.TopUpRequestDto;
import com.nextword.backend.feature.user.services.WalletServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/wallet")
@CrossOrigin(origins = "*")
public class WalletController {
    private final WalletServices walletServices;

    public WalletController(WalletServices walletServices) {
        this.walletServices = walletServices;
    }

    @PostMapping("/topup")
    public ResponseEntity<String> topUpWallet(@RequestBody TopUpRequestDto request) {
        String message = walletServices.addFunds(request);
        return ResponseEntity.ok(message);
    }

}
