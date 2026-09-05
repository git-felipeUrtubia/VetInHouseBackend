package org.example.backend_vet_in_house.appointments.service;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.GenerateSlotsReqDTO;
import org.example.backend_vet_in_house.appointments.model.AvailableSlot;
import org.example.backend_vet_in_house.appointments.repository.AvailableSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableSlotService {

    private final AvailableSlotRepository availableSlotRepository;

    @Transactional
    public String generateSlotsForDay(GenerateSlotsReqDTO req) {
        if (availableSlotRepository.existsBySlotDate(req.date())) {
            throw new RuntimeException("Ya existen horarios generados para esta fecha.");
        }

        List<AvailableSlot> slotsToSave = new ArrayList<>();
        LocalTime currentTime = req.startTime();

        // Bucle para generar bloques hasta llegar a la hora de fin
        while (currentTime.plusMinutes(req.durationMinutes()).isBefore(req.endTime()) ||
                currentTime.plusMinutes(req.durationMinutes()).equals(req.endTime())) {

            AvailableSlot slot = AvailableSlot.builder()
                    .slotDate(req.date())
                    .startTime(currentTime)
                    .endTime(currentTime.plusMinutes(req.durationMinutes()))
                    .isAvailable(true)
                    .build();

            slotsToSave.add(slot);
            currentTime = currentTime.plusMinutes(req.durationMinutes());
        }

        availableSlotRepository.saveAll(slotsToSave);
        return "Horarios generados exitosamente para el día " + req.date();
    }

    public List<AvailableSlot> getAvailableSlotsByDate(LocalDate date) {
        return availableSlotRepository.findAvailableSlotsByDate(date);
    }

    @Transactional
    public void deleteSlot(Long slotId) {
        AvailableSlot slot = availableSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        availableSlotRepository.delete(slot);
    }
}