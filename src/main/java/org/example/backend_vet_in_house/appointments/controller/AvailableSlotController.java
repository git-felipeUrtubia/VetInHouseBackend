package org.example.backend_vet_in_house.appointments.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.GenerateSlotsReqDTO;
import org.example.backend_vet_in_house.appointments.service.AvailableSlotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/schedule")
@RequiredArgsConstructor
public class AvailableSlotController {

    private final AvailableSlotService availableSlotService;

    // Endpoint para el ADMIN: Genera los bloques de una jornada
    @PostMapping("/create")
    public ResponseEntity<?> generateSlots(@RequestBody GenerateSlotsReqDTO req) {
        try {
            return new ResponseEntity<>(availableSlotService.generateSlotsForDay(req), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para el USUARIO: Obtiene los horarios disponibles de un día en el calendario
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return new ResponseEntity<>(availableSlotService.getAvailableSlotsByDate(date), HttpStatus.OK);
    }

    // Endpoint para el ADMIN: Elimina un horario específico si necesita bloquearlo
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSlot(@RequestParam Long slotId) {
        availableSlotService.deleteSlot(slotId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}