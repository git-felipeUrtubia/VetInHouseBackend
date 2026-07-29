package org.example.backend_vet_in_house.pets.dto.res;

public record PetResDTO(
        Long userIdRef,
        String patientNumber,
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
