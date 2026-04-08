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
@Table(name="Reserva")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @Column(name = "id_reserva", length = 36, nullable = false)
    private String id;
    @ManyToOne
    @JoinColumn(name = "id_slot", nullable = false)
    private SlotAvailable slot;
    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private User student;
    @Column(name = "estatus_reserva", length = 30, nullable = false)
    private String status;
    @Column(name = "link_meet", length = 255)
    private String meetLink;
    @Column(name = "asistencia_estudiante", length = 1)
    private String studentAttendance;
    @Column(name = "asistencia_profesor", length = 1)
    private String teacherAttendance;
    @Column(name = "fecha_creacion", nullable = false)
    private ZonedDateTime createdAt;
    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;
    @Column(name = "tema_clase", length = 500)
    private String topic;

    @PrePersist
    protected void onCreate() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = ZonedDateTime.now();
        this.status = "PENDING";
    }
}
