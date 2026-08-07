package org.example.backend_vet_in_house.pets.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.pets.dto.req.SavePetReqDTO;
import org.example.backend_vet_in_house.pets.dto.req.UpdatePetReqDTO;
import org.example.backend_vet_in_house.pets.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping("/create")
    public ResponseEntity<?> savedPet(@RequestBody SavePetReqDTO req) {
        return new ResponseEntity<>(petService.savePet(req), HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllPets() {
        return new ResponseEntity<>(petService.findAllPets(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePetByPacientNumber(
            @RequestBody UpdatePetReqDTO req,
            @RequestParam String pacientNumber)
    {
        return new ResponseEntity<>(petService.updatePetByPacientNumber(req, pacientNumber), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePetByPatientNumber(@RequestParam String patientNumber) {
        return new ResponseEntity<>(petService.deletePetByPacientNumber(patientNumber), HttpStatus.OK);
    }
}
