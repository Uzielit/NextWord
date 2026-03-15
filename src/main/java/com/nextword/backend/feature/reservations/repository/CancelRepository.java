package com.nextword.backend.feature.reservations.repository;


import com.nextword.backend.feature.reservations.entity.Cancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancelRepository extends JpaRepository<Cancel, String> {

    List<Cancel> findAll();
}
