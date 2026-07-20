package org.example.backend_vet_in_house.shared.exception.catalog;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
