package org.example.backend_vet_in_house.pets.dto.req;

public record UpdatePetReqDTO(
        String name,
        int age,
        double weight,
        String specie,
        String gender,
        String breed,
        boolean isNeutered,
        String allergies,
        String microchipNumber
) {
}
