package com.nextword.backend.feature.reservations.services;



import com.nextword.backend.feature.reservations.dto.SlotAvailableDto;
import com.nextword.backend.feature.reservations.dto.SlotResponseDto;
import com.nextword.backend.feature.reservations.entity.SlotAvailable;
import com.nextword.backend.feature.reservations.repository.SlotAvailableRepository;
import com.nextword.backend.feature.user.entity.User;

import com.nextword.backend.feature.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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
