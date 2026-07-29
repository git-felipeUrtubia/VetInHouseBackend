package org.example.backend_vet_in_house.shared.exception.appointment;

public class AppointmentAlreadyExistException extends RuntimeException {
    public AppointmentAlreadyExistException(String message) {
        super(message);
    }
}
