package com.nextword.backend.feature.reservations.services;

import com.nextword.backend.feature.reservations.dto.ReviewRequestDto;
import com.nextword.backend.feature.reservations.entity.Reservation;
import com.nextword.backend.feature.reservations.entity.Review;
import com.nextword.backend.feature.reservations.repository.ReservationRepository;
import com.nextword.backend.feature.reservations.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class ReviewServices {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public ReviewServices(ReviewRepository reviewRepository, ReservationRepository reservationRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public String createReview(ReviewRequestDto request) {

        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!"COMPLETED".equals(reservation.getStatus())) {
            throw new RuntimeException("No puedes calificar una clase que no ha terminado.");
        }

        if (request.rating() < 1 || request.rating() > 5) {
            throw new RuntimeException("La calificación debe estar entre 1 y 5");
        }


        Review review = new Review();
        review.setReservation(reservation);
        review.setRating(request.rating());
        review.setComment(request.comentary());

        reviewRepository.save(review);

        return "Termianado Review";
    }
}
