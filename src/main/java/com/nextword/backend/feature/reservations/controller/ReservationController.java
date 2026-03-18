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
import java.util.List;

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
    public ResponseEntity<String> createSlot(@RequestBody SlotAvailableDto request) {
        String generatedSlotId = slotAvailableServices.createTeacherSlot(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Slot de horario creado con exito id es " + generatedSlotId);
    }

    @GetMapping("/slots/available")
    public ResponseEntity<List<SlotResponseDto>>getAvailableSlots(){
        List<SlotResponseDto> availableSlots = slotAvailableServices.getAvailableSlots();
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSlot(@RequestBody ReservationDto request) {
        String reservationId = reservationServices.CreateReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Reserva hecha , el id es  " + reservationId);
    }

    @GetMapping("/myClass")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(
            Principal principal,
            @RequestParam(required = false) String status) {

        String email = principal.getName();
        User usuarioLogueado = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<ReservationResponseDto> myReservations = reservationServices.getStudentReservations(usuarioLogueado.getId(), status);

        return ResponseEntity.ok(myReservations);
    }
    @PutMapping("/complete")
    public ResponseEntity<String> completeClass(@RequestBody CompleteClassRequest request) {
        String message = reservationServices.completeReservation(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelRequestDto request) {
        String message = cancelServices.processCancellation(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/myAgenda")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<List<ReservationResponseDto>> getStudentAgenda(Principal principal) {
        User usuario = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(reservationServices.getUpcomingAgenda(usuario.getId(), false));
    }
    @GetMapping("/teacherAgenda")
    @PreAuthorize("hasRole('PROFESOR')")
    public ResponseEntity<List<ReservationResponseDto>> getTeacherAgenda(Principal principal) {
        User usuario = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(reservationServices.getUpcomingAgenda(usuario.getId(), true));
    }

}
