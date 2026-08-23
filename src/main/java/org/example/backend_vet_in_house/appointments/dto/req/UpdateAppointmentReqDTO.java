package org.example.backend_vet_in_house.appointments.dto.req;

import java.time.LocalDateTime;

public record UpdateAppointmentReqDTO(
        String reasonForVisit,
        LocalDateTime appointmentDate,
        LocalDateTime updateAt,
        String serviceType,
        String status
) {
}
