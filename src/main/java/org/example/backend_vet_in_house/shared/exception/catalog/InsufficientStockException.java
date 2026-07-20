package org.example.backend_vet_in_house.shared.exception.catalog;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
