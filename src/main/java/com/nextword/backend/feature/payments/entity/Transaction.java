package com.nextword.backend.feature.payments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name="Transaccion_Pago")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @Column(name = "id_transaccion", length = 36, nullable = false)
    private String id;
    @Column(name = "id_reserva", length = 36, nullable = false)
    private String reservationId;
    @Column(name = "monto_total", nullable = false)
    private BigDecimal totalAmount;
    @Column(name = "referencia_mercadopago", length = 255)
    private String mercadoPagoReference;
    @Column(name = "estado_pago", length = 30, nullable = false)
    private String paymentStatus;
    @Column(name = "url_recibo", length = 500)
    private String receiptUrl;
    @Column(name = "comision_plataforma", nullable = false)
    private BigDecimal platformCommission;
    @Column(name = "ganancia_profesor", nullable = false)
    private BigDecimal teacherProfit;
    @Column(name = "fecha_transaccion", nullable = false)
    private ZonedDateTime transactionDate;
}
