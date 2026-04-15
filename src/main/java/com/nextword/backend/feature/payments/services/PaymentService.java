package com.nextword.backend.feature.payments.services;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.nextword.backend.feature.payments.entity.Transaction;
import com.nextword.backend.feature.payments.repository.PaymentRepository;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;


    public PaymentService(UserRepository userRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        MercadoPagoConfig.setAccessToken("APP_USR-5590402425687116-041223-759a2283076a3276fcb8a4456e470e44-2682147526");
    }

    @Transactional
    public String claimStaticLinkPayment(String userId, Long paymentId) throws Exception {

        if (paymentRepository.findByMercadoPagoReference(paymentId.toString()).isPresent()) {
            throw new RuntimeException("Este recibo ya fue reclamado y cobrado anteriormente.");
        }

        PaymentClient client = new PaymentClient();
        Payment payment;

        try {
            payment = client.get(paymentId);
        } catch (com.mercadopago.exceptions.MPApiException ex) {
            throw new RuntimeException("El recibo no existe o no pertenece a esta cuenta. Detalles: " + ex.getMessage());
        }

        if (!"approved".equalsIgnoreCase(payment.getStatus())) {
            throw new RuntimeException("El pago existe pero su estado es: " + payment.getStatus());
        }

        BigDecimal amountPaid = payment.getTransactionAmount();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));

        BigDecimal currentBalance = user.getWalletBalance() != null ? user.getWalletBalance() : BigDecimal.ZERO;
        user.setWalletBalance(currentBalance.add(amountPaid));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUserId(userId);
        transaction.setTotalAmount(amountPaid);
        transaction.setMercadoPagoReference(paymentId.toString());
        transaction.setPaymentStatus("COMPLETED");
        transaction.setTransactionDate(ZonedDateTime.now());
        paymentRepository.save(transaction);

        return "¡Éxito! Se han agregado $" + amountPaid + " a tu monedero.";
    }
}
