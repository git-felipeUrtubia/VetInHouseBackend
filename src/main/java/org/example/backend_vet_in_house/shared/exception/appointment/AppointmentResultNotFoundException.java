package org.example.backend_vet_in_house.shared.exception.appointment;

public class AppointmentResultNotFoundException extends RuntimeException {
    public AppointmentResultNotFoundException(String message) {
        super(message);
    }
}
