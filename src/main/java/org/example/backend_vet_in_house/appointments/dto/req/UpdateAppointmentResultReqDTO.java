package org.example.backend_vet_in_house.appointments.dto.req;

public record UpdateAppointmentResultReqDTO(
        String diagnosis,
        String treatment
) {
}
