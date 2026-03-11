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
@Table(name = "Resenia")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @Column(name = "id_resenia", length = 36, nullable = false)
    private String id;
    @Column(name = "id_reserva", length = 36, nullable = false)
    private String reservationId;
    @Column(name = "calificacion", nullable = false)
    private Integer rating;
    @Column(name = "comentario", length = 500)
    private String comment;
    @Column(name = "fecha_resenia", nullable = false)
    private ZonedDateTime reviewDate;
}
