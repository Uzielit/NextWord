package com.nextword.backend.feature.reservations.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "Resenia")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @Column(name = "id_resenia", length = 36, nullable = false)
    private String id;
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reservation reservation;
    @Column(name = "calificacion", nullable = false)
    private Integer rating;
    @Column(name = "comentario", length = 500)
    private String comment;
    @Column(name = "fecha_resenia", nullable = false)
    private ZonedDateTime reviewDate;
    @PrePersist
    protected void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
        this.reviewDate = ZonedDateTime.now();
    }
}
