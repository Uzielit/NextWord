package com.nextword.backend.feature.reservations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name="Reserva")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @Column(name = "id_reserva", length = 36, nullable = false)
    private String id;
    @Column(name = "id_slot", length = 36, nullable = false)
    private String slotId;
    @Column(name = "id_estudiante", length = 36, nullable = false)
    private String studentId;
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
}
