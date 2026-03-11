package com.nextword.backend.feature.reservations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "Slot_Disponibles")
@Getter
@Setter
@NoArgsConstructor

public class SlotAvailable {
    @Id
    @Column(name = "id_slot", length = 36, nullable = false)
    private String id;
    @Column(name = "id_profesor", length = 36, nullable = false)
    private String teacherId;
    @Column(name = "fecha_slot", nullable = false)
    private LocalDate slotDate;
    @Column(name = "hora_inicio", length = 5, nullable = false)
    private String startTime;
    @Column(name = "hora_fin", length = 5, nullable = false)
    private String endTime;
    @Column(name = "tipo_clase", length = 50)
    private String classType;
    @Column(name = "estado", length = 20, nullable = false)
    private String status;
}
