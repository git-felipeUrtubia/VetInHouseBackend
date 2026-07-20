package org.example.backend_vet_in_house.shared.exception.catalog;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
