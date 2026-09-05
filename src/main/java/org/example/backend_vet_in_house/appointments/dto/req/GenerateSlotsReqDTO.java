package org.example.backend_vet_in_house.appointments.dto.req;

import java.time.LocalDate;
import java.time.LocalTime;

public record GenerateSlotsReqDTO(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        int durationMinutes
) {
}
