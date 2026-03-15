package com.nextword.backend.feature.reservations.entity;

import com.nextword.backend.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "Slot_Disponibles")
@Getter
@Setter
@NoArgsConstructor

public class SlotAvailable {
    @Id
    @Column(name = "id_slot", length = 36, nullable = false)
    private String id;
    @ManyToOne
    @JoinColumn(name = "id_profesor", nullable = false)
    private User teacher;
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

    @PrePersist
    protected void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }

}
