package org.example.backend_vet_in_house.users.dto.res;

import java.time.LocalDateTime;

public record ItemAppointmentFromUserResDTO(
        String codeService,
        String servicio,
        LocalDateTime appointmentDate,
        LocalDateTime createAt,
        String status
) {
}
