package org.example.backend_vet_in_house.pets.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.model.Appointment;
import org.example.backend_vet_in_house.appointments.repository.AppointmentRepository;
import org.example.backend_vet_in_house.appointments.repository.AvailableSlotRepository;
import org.example.backend_vet_in_house.pets.dto.req.SavePetReqDTO;
import org.example.backend_vet_in_house.pets.dto.req.UpdatePetReqDTO;
import org.example.backend_vet_in_house.pets.dto.res.PetResDTO;
import org.example.backend_vet_in_house.pets.model.Gender;
import org.example.backend_vet_in_house.pets.model.Pet;
import org.example.backend_vet_in_house.pets.model.Specie;
import org.example.backend_vet_in_house.pets.repository.PetRepository;
import org.example.backend_vet_in_house.shared.exception.pet.PetAlreadyExistException;
import org.example.backend_vet_in_house.shared.exception.pet.PetNotFoundException;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserEntityRepository userEntityRepository;
    private final AppointmentRepository appointmentRepository;
    private final AvailableSlotRepository availableSlotRepository;

    public String savePet(SavePetReqDTO req) {


        UserEntity user = userEntityRepository.findUserByUsername(req.username())
                .orElseThrow(() -> new UserNotFoundException("User " + req.username() + " not found"));


        boolean checkPet = petRepository.findPetByPatientNumber(req.patientNumber()).isPresent();

        if(checkPet) {
            throw new PetAlreadyExistException("Pet " + req.patientNumber() + " already exist");
        }

        petRepository.save(
                Pet.builder()
                        .userIdRef(user.getUserId())
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

    public String updatePetByPacientNumber(UpdatePetReqDTO req, String patientNumber) {

        Pet pet = petRepository.findPetByPatientNumber(patientNumber)
                .orElseThrow(() -> new PetNotFoundException("Pet " + patientNumber + " not found"));

        pet.setName(req.name());
        pet.setAge(req.age());
        pet.setWeight(req.weight());
        pet.setSpecie(Specie.valueOf(req.specie()));
        pet.setGender(Gender.valueOf(req.gender()));
        pet.setBreed(req.breed());
        pet.setNeutered(req.isNeutered());
        pet.setAllergies(req.allergies());
        pet.setMicrochipNumber(req.microchipNumber());

        petRepository.save(pet);

        return "Pet update with successfully!";
    }

    @Transactional
    public String deletePetByPacientNumber(String patientNumber) {
        Pet pet = petRepository.findPetByPatientNumber(patientNumber)
                .orElseThrow(() -> new PetNotFoundException("Pet " + patientNumber + " not found"));

        // 1. Buscar todas las citas asociadas a esta mascota
        List<Appointment> appointments = appointmentRepository.findAllByPet(pet.getPetId());

        // 2. Iterar sobre las citas para liberar los horarios y eliminarlas
        for (Appointment ap : appointments) {

            // Liberar el horario reservado
            availableSlotRepository.findBySlotDateAndStartTime(
                    ap.getAppointmentDate().toLocalDate(),
                    ap.getAppointmentDate().toLocalTime()
            ).ifPresent(slot -> {
                slot.setAvailable(true);
                availableSlotRepository.save(slot);
            });

            // Eliminar la cita (JPA eliminará automáticamente el AppointmentResult asociado por el CascadeType.ALL)
            appointmentRepository.delete(ap);
        }

        // 3. Finalmente eliminar la mascota de forma segura
        petRepository.deleteById(pet.getPetId());

        return "Pet deleted successfully with all its associated appointments";
    }
}
