package org.example.backend_vet_in_house.appointments.dto.req;

import java.time.LocalDateTime;

public record CreateAppointmentReqDTO(
        String patientNumber,
        String codeService,
        String reasonForVisit,
        LocalDateTime appointmentDate,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String serviceType,
        String status
) {
}
