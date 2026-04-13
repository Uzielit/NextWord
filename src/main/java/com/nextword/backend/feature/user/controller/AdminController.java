package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.request.admin.AdminDashboardResponse;
import com.nextword.backend.feature.user.dto.request.admin.CreateTeacherRequest;
import com.nextword.backend.feature.user.dto.request.admin.UpdateProfileRequest;
import com.nextword.backend.feature.user.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponse> getDashboardStats() {
        AdminDashboardResponse stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createTeacher(@RequestBody CreateTeacherRequest req) {
        adminService.createTeacher(req);
        return ResponseEntity.ok("Profesor creado exitosamente");
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable String id, @RequestBody UpdateProfileRequest req) {
        adminService.updateProfile(id, req);
        return ResponseEntity.ok("Perfil actualizado");
    }

}
