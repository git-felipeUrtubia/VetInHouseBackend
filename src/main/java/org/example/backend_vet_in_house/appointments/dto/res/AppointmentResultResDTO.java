package org.example.backend_vet_in_house.appointments.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AppointmentResultResDTO(
        String codeService,
        String diagnosis,
        String treatment,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt
) {
}
