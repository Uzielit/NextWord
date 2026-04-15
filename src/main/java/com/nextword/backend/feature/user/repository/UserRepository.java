package com.nextword.backend.feature.user.repository;

import com.nextword.backend.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);


    long countByRoleId(Integer roleId);

    long countByRoleIdAndRegistrationDateAfter(Integer roleId, ZonedDateTime date);
}
