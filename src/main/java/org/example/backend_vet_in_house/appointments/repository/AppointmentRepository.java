package org.example.backend_vet_in_house.appointments.repository;

import org.example.backend_vet_in_house.appointments.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT ap FROM Appointment ap WHERE ap.codeService = :code")
    Optional<Appointment> findAppointmentByCode(@Param("code") String code);

    @Query("SELECT ap FROM Appointment ap WHERE ap.petIdRef = :id")
    List<Appointment> findAllByPet(@Param("id") Long id);
}
