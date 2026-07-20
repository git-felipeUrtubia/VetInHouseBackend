package org.example.backend_vet_in_house.shared.exception.sales;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
