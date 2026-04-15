package com.nextword.backend.feature.user.controller;

import com.nextword.backend.feature.user.dto.request.admin.*;
import com.nextword.backend.feature.user.services.AdminService;
import com.nextword.backend.feature.user.services.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final PdfExportService pdfExportService;

    public AdminController(AdminService adminService,
                           PdfExportService pdfExportService
    ) {
        this.adminService = adminService;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponse> getDashboardStats() {
        AdminDashboardResponse stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/teachers")
    public ResponseEntity<?> createTeacher(@RequestBody CreateTeacherRequest req) {
        try {
            adminService.createTeacher(req);
            return ResponseEntity.ok(Map.of("message", "Profesor creado exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @RequestBody UpdateProfileRequest req,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        adminService.updateProfile(userId, req);

        return ResponseEntity.ok("Perfil actualizado");
    }

    @GetMapping("/classes/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClassHistoryResponse>> getClassHistory() {
        return ResponseEntity.ok(adminService.getClassHistory());
    }

    // Endpoint para la pestaña "Reportes"
    @GetMapping("/reports/financial")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FinancialReportResponse> getFinancialReports() {
        return ResponseEntity.ok(adminService.getFinancialReports());
    }
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDirectoryResponse>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/teachers/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> toggleTeacherStatus(
            @PathVariable String id,
            @RequestParam String status) {
        adminService.toggleTeacherStatus(id, status);
        return ResponseEntity.ok("Estatus actualizado a " + status);
    }

    @GetMapping("/reports/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=reporte_nextword.pdf";
        response.setHeader(headerKey, headerValue);

        FinancialReportResponse data = adminService.getFinancialReports();
        pdfExportService.export(response, data);
    }
}

