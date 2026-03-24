package com.nextword.backend.feature.reservations.repository;



import com.nextword.backend.feature.reservations.entity.Reservation;
import com.nextword.backend.feature.reservations.entity.SlotAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface ReservationRepository extends JpaRepository<Reservation,String> {
    List<Reservation> findByStudentId(String studentId);

    List<Reservation> findByStudentIdAndStatus(String studentId, String status);


    List<Reservation> findByStudentIdAndSlotSlotDateGreaterThanEqualOrderBySlotSlotDateAsc(
            String studentId, LocalDate date);

    List<Reservation> findBySlotTeacherIdAndSlotSlotDateGreaterThanEqualOrderBySlotSlotDateAsc(
            String teacherId, LocalDate date);


}



