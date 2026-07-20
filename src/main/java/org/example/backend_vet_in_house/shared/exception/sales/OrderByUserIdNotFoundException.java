package org.example.backend_vet_in_house.shared.exception.sales;

public class OrderByUserIdNotFoundException extends RuntimeException {
    public OrderByUserIdNotFoundException(String message) {
        super(message);
    }
}
