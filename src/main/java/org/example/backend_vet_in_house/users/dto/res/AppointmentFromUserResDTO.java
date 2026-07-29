package org.example.backend_vet_in_house.users.dto.res;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentFromUserResDTO(
        String firstName,
        String lastName,
        String username,
        List<ItemPetFromUserResDTO> pet
) {
}
