package com.nextword.backend.feature.reservations.controller;

import com.nextword.backend.feature.reservations.dto.*;
import com.nextword.backend.feature.reservations.services.CancelServices;
import com.nextword.backend.feature.reservations.services.ReservationServices;
import com.nextword.backend.feature.reservations.services.SlotAvailableServices;
import com.nextword.backend.feature.user.entity.User;
import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final SlotAvailableServices slotAvailableServices;
    private final CancelServices cancelServices;

    private final ReservationServices reservationServices;
    private final UserRepository userRepository;

    public ReservationController(SlotAvailableServices slotAvailableServices,
                                 ReservationServices reservationServices,
                                 CancelServices cancelServices,
                                 UserRepository userRepository) {
        this.slotAvailableServices = slotAvailableServices;
        this.reservationServices = reservationServices;
        this.cancelServices = cancelServices;
        this.userRepository = userRepository;
    }
    @PostMapping("/slot")
    public ResponseEntity<Map<String, String>> createSlot(@RequestBody CreateSlotRequest request) {
        String generatedSlotId = slotAvailableServices.createTeacherSlot(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("Slot generado con Id ", generatedSlotId));
    }

    @GetMapping("/slots/available")
    public ResponseEntity<List<SlotResponse>>getAvailableSlots(){
        List<SlotResponse> availableSlots = slotAvailableServices.getAvailableSlots();
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSlot(@RequestBody BookClassRequest request) {
        String reservationId = reservationServices.CreateReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Reserva hecha , el id es  " + reservationId);
    }

    @GetMapping("/slots/filter")
    public ResponseEntity<List<SlotResponse>> getSlotsByRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String teacherId) {

        List<SlotResponse> slots = slotAvailableServices.getFilteredAvailableSlots(startDate, endDate, teacherId);
        return ResponseEntity.ok(slots);
    }

    @GetMapping("/myClass")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            Principal principal,
            @RequestParam(required = false) String status) {

        String email = principal.getName();
        User usuarioLogueado = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String dbStatus = status;
        if (status != null) {
            switch (status.toLowerCase()) {
                case "Pendientes": dbStatus = "Pagado"; break;
                case "Completadas": dbStatus = "Completada"; break;
                case "Canceladas": dbStatus = "Cancelada"; break;

            }
        }
        List<ReservationResponse> myReservations = reservationServices.getStudentReservations(usuarioLogueado.getId(), dbStatus);

        return ResponseEntity.ok(myReservations);
    }
    @PutMapping("/complete")
    public ResponseEntity<String> completeClass(@RequestBody CompleteClassRequest request) {
        String message = reservationServices.completeReservation(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelClassRequest request) {
        String message = cancelServices.processCancellation(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/myAgenda")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<List<ReservationResponse>> getStudentAgenda(Principal principal) {
        User usuario = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(reservationServices.getUpcomingAgenda(usuario.getId(), false));
    }
    @GetMapping("/teacherAgenda")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<List<ReservationResponse>> getTeacherAgenda(Principal principal) {
        User usuario = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(reservationServices.getUpcomingAgenda(usuario.getId(), true));
    }

}
