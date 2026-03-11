package com.nextword.backend.feature.reservations.entity;

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
@Table(name="Cancelacion_Reprogramacion")
@Getter
@Setter
@NoArgsConstructor
public class Cancel {
    @Id
    @Column(name = "id_movimiento", length = 36, nullable = false)
    private String id;
    @Column(name = "id_reserva", length = 36, nullable = false)
    private String reservationId;
    @Column(name = "id_solicitante", length = 36, nullable = false)
    private String requesterId;
    @Column(name = "tipo_accion", length = 20)
    private String actionType;
    @Column(name = "motivo", length = 1000)
    private String reason;
    @Column(name = "monto_reembolso")
    private BigDecimal refundAmount;
    @Column(name = "monto_penalizacion")
    private BigDecimal penaltyAmount;
    @Column(name = "estatus_solicitud", length = 30)
    private String requestStatus;
}
