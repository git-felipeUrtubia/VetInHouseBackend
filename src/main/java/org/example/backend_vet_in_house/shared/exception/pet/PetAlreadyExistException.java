package org.example.backend_vet_in_house.shared.exception.pet;

public class PetAlreadyExistException extends RuntimeException {
    public PetAlreadyExistException(String message) {
        super(message);
    }
}
