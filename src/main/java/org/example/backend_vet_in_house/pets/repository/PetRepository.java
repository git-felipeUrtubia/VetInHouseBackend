package org.example.backend_vet_in_house.pets.repository;

import org.example.backend_vet_in_house.pets.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p WHERE p.patientNumber = :patientNumber")
    Optional<Pet> findPetByPatientNumber(@Param("patientNumber") String patientNumber);

    @Query("SELECT pet FROM Pet pet WHERE pet.userIdRef = :id")
    List<Pet> findPetsByUserId(@Param("id") Long id);
}
