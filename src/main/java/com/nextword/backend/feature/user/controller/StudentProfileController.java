package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.entity.StudentProfile;
import com.nextword.backend.feature.user.repository.StudentProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentProfileController {
    private final StudentProfileRepository studentProfileRepository;

    public StudentProfileController(StudentProfileRepository studentProfileRepository) {
        this.studentProfileRepository = studentProfileRepository;
    }

    @GetMapping
    public List<StudentProfile> getAllStudents() {
        return studentProfileRepository.findAll();
    }


}
