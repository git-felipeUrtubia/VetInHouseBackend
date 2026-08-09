package org.example.backend_vet_in_house.shared.exception.sales;

public class CommuneNotFoundException extends RuntimeException {
    public CommuneNotFoundException(String message) {
        super(message);
    }
}
