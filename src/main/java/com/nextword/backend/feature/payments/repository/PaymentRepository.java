package com.nextword.backend.feature.payments.repository;

import com.nextword.backend.feature.payments.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Transaction, String>{

    Optional<Transaction> findByMercadoPagoReference(String mercadoPagoReference);

}


