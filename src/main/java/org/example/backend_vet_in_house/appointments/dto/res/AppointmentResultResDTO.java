package org.example.backend_vet_in_house.appointments.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AppointmentResultResDTO(
        String codeService,
        String diagnosis,
        String treatment,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime createdAt
) {
}
