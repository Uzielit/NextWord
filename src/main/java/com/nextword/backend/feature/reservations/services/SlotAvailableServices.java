package com.nextword.backend.feature.reservations.services;



import com.nextword.backend.feature.reservations.dto.SlotAvailableDto;
import com.nextword.backend.feature.reservations.dto.SlotResponseDto;
import com.nextword.backend.feature.reservations.entity.SlotAvailable;
import com.nextword.backend.feature.reservations.repository.SlotAvailableRepository;
import com.nextword.backend.feature.user.entity.User;

import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SlotAvailableServices {

    private final SlotAvailableRepository slotRepository;
    private final UserRepository userRepository;

    public SlotAvailableServices(SlotAvailableRepository slotRepository, UserRepository userRepository) {
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    public String createTeacherSlot(SlotAvailableDto request) {

        User teacher = userRepository.findById(request.teacherId())
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con el ID: " + request.teacherId()));

        LocalTime startTime = LocalTime.parse(request.startTime());
        LocalTime endTime = LocalTime.parse(request.endTime());
        if (!startTime.isBefore(endTime)) {
            throw new RuntimeException("La hora de inicio debe ser anterior a la hora de fin.");
        }
        LocalDateTime slotStartDateTime = LocalDateTime.of(request.slotDate(), startTime);
        if (slotStartDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No puedes programar una clase antes de hoy ");
        }
        List<SlotAvailable> existingSlots = slotRepository.findByTeacherIdAndSlotDate(teacher.getId(), request.slotDate());

        for (SlotAvailable existingSlot : existingSlots) {
            LocalTime existingStart = LocalTime.parse(existingSlot.getStartTime());
            LocalTime existingEnd = LocalTime.parse(existingSlot.getEndTime());

            if (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart)) {
                throw new RuntimeException("Ya tienes una clase registrada de " + existingSlot.getStartTime() + " a " + existingSlot.getEndTime());
            }
        }

        SlotAvailable newSlot = new SlotAvailable();
        newSlot.setTeacher(teacher);
        newSlot.setSlotDate(request.slotDate());
        newSlot.setStartTime(request.startTime());
        newSlot.setEndTime(request.endTime());
        newSlot.setClassType(request.classType());
        newSlot.setStatus("DISPONIBLE");


        SlotAvailable savedSlot = slotRepository.save(newSlot);


        return savedSlot.getId();
    }
    public List<SlotResponseDto> getFilteredAvailableSlots(LocalDate start, LocalDate end, String teacherId) {
        List<SlotAvailable> slots;
        if (teacherId != null && !teacherId.isBlank()) {
            slots = slotRepository.findByTeacherIdAndStatusAndSlotDateBetweenOrderBySlotDateAscStartTimeAsc(
                    teacherId, "DISPONIBLE", start, end);
        } else {
            slots = slotRepository.findByStatusAndSlotDateBetweenOrderBySlotDateAscStartTimeAsc(
                    "DISPONIBLE", start, end);
        }

        return slots.stream()
                .map(slot -> new SlotResponseDto(
                        slot.getId(),
                        slot.getTeacher().getFullName(),
                        slot.getSlotDate(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        slot.getClassType()
                )).collect(Collectors.toList());
    }

    public List<SlotResponseDto> getAvailableSlots() {
        return slotRepository.findByStatus("DISPONIBLE")
                .stream()
                .map(slot -> new SlotResponseDto(
                        slot.getId(),
                        slot.getTeacher().getFullName(),
                        slot.getSlotDate(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        slot.getClassType()
                ))
                .collect(Collectors.toList());
    }
}
