package org.example.backend_vet_in_house.users.dto.res;

public record ItemPetUser(
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
