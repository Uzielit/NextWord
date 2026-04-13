package com.nextword.backend.feature.payments.repository;

import com.nextword.backend.feature.payments.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Transaction, String>{

    Optional<Transaction> findByMercadoPagoReference(String mercadoPagoReference);

    @Query("SELECT COALESCE(SUM(t.totalAmount), 0) FROM Transaction t WHERE t.transactionDate >= :startDate")
    BigDecimal sumIncomeAfterDate(@Param("startDate") ZonedDateTime startDate);
}


