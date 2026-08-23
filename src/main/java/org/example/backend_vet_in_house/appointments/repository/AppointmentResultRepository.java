package org.example.backend_vet_in_house.appointments.repository;

import org.example.backend_vet_in_house.appointments.model.AppointmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentResultRepository extends JpaRepository<AppointmentResult, Long> {

    Optional<AppointmentResult> findByAppointment_AppointmentId(Long appointmentId);

    boolean existsByAppointment_AppointmentId(Long appointmentId);
}
