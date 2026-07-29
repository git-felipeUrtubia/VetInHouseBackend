package org.example.backend_vet_in_house.appointments.dto.res;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "codeService",
        "patientNumber",
        "namePatient",
        "weight",
        "age",
        "reasonForVisit",
        "appointmentDate",
        "createAt",
        "updateAt",
        "serviceType",
        "status"
})
public record AppointmentResDTO(
        String patientNumber,
        String namePatient,
        double weight,
        int age,
        String codeService,
        String reasonForVisit,
        LocalDateTime appointmentDate,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String serviceType,
        String status
) {
}
