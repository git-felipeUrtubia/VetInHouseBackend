package org.example.backend_vet_in_house.shared.exception.shipping;

public class CommuneNotBelongToRegion extends RuntimeException {
    public CommuneNotBelongToRegion(String message) {
        super(message);
    }
}
