package com.nextword.backend.feature.reservations.entity;

import com.nextword.backend.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name="Cancelacion_Reprogramacion")
@Getter
@Setter
@NoArgsConstructor
public class Cancel {
    @Id
    @Column(name = "id_movimiento", length = 36, nullable = false)
    private String id;
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reservation reservation;
    @ManyToOne
    @JoinColumn(name = "id_solicitante", nullable = false)
    private User requester;
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


    @PrePersist
    protected void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }
}
