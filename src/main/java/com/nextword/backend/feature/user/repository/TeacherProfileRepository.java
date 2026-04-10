package com.nextword.backend.feature.user.repository;


import com.nextword.backend.feature.user.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherProfileRepository extends JpaRepository<TeacherProfile,String> {


}
