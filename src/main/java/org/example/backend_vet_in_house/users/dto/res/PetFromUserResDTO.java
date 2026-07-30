package org.example.backend_vet_in_house.users.dto.res;

import java.util.List;

public record PetFromUserResDTO(
        String firstName,
        String lastName,
        String username,
        List<ItemPetUser> pets
) {
}
