package org.example.backend_vet_in_house.users.dto.res;

import java.util.List;

public record ItemPetFromUserResDTO(
        String patientNumber,
        String namePatient,
        List<ItemAppointmentFromUserResDTO> appointments
) {
}
