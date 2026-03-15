package com.nextword.backend.feature.reservations.repository;

import com.nextword.backend.feature.reservations.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

}
