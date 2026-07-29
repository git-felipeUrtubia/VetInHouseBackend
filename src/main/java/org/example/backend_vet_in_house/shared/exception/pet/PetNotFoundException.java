package org.example.backend_vet_in_house.shared.exception.pet;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(String message) {
        super(message);
    }
}
