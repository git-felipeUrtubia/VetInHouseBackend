package org.example.backend_vet_in_house.shared.exception.sales;

public class RegionNotFoundException extends RuntimeException {
    public RegionNotFoundException(String message) {
        super(message);
    }
}
