package org.example.backend_vet_in_house.pets.service;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.pets.dto.req.SavePetReqDTO;
import org.example.backend_vet_in_house.pets.dto.res.PetResDTO;
import org.example.backend_vet_in_house.pets.model.Gender;
import org.example.backend_vet_in_house.pets.model.Pet;
import org.example.backend_vet_in_house.pets.model.Specie;
import org.example.backend_vet_in_house.pets.repository.PetRepository;
import org.example.backend_vet_in_house.shared.exception.pet.PetAlreadyExistException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public String savePet(SavePetReqDTO req) {

        boolean checkPet = petRepository.findPetByPatientNumber(req.patientNumber()).isPresent();

        if(checkPet) {
            throw new PetAlreadyExistException("Pet " + req.patientNumber() + " already exist");
        }

        petRepository.save(
                Pet.builder()
                        .userIdRef(req.userIdRef())
                        .patientNumber(req.patientNumber())
                        .name(req.name())
                        .age(req.age())
                        .weight(req.weight())
                        .specie(Specie.valueOf(req.specie()))
                        .gender(Gender.valueOf(req.gender()))
                        .breed(req.breed())
                        .isNeutered(req.isNeutered())
                        .allergies(req.allergies())
                        .microchipNumber(req.microchipNumber())
                        .build()
        );

        return "Pet saved with successfully!";
    }

    public List<PetResDTO> findAllPets() {
        return petRepository.findAll().stream()
                .map(pet -> new PetResDTO(
                        pet.getUserIdRef(),
                        pet.getPatientNumber(),
                        pet.getName(),
                        pet.getAge(),
                        pet.getWeight(),
                        pet.getSpecie().name(),
                        pet.getGender().name(),
                        pet.getBreed(),
                        pet.isNeutered(),
                        pet.getAllergies(),
                        pet.getMicrochipNumber()
                )).toList();
    }

}
