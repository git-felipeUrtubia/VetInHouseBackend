package org.example.backend_vet_in_house.appointments.dto.res;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;
import java.util.List;


@JsonPropertyOrder({
        "firstName",
        "lastName",
        "username",
        "phone",
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
public record AppointmentWithUserResDTO(
        String firstName,
        String lastName,
        String username,
        List<String> phone,
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
