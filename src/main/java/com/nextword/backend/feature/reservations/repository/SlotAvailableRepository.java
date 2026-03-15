package com.nextword.backend.feature.reservations.repository;


import com.nextword.backend.feature.reservations.entity.SlotAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotAvailableRepository extends JpaRepository<SlotAvailable, String> {

    List<SlotAvailable> findByStatus(String status);

}
