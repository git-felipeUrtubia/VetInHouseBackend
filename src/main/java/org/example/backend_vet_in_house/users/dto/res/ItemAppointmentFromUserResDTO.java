package org.example.backend_vet_in_house.users.dto.res;

import org.example.backend_vet_in_house.appointments.dto.res.AppointmentResultResDTO;

import java.time.LocalDateTime;

public record ItemAppointmentFromUserResDTO(
        String codeService,
        String servicio,
        LocalDateTime appointmentDate,
        LocalDateTime createAt,
        String status,
        AppointmentResultResDTO result
) {
}
