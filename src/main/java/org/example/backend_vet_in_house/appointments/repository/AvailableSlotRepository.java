package org.example.backend_vet_in_house.appointments.repository;

import org.example.backend_vet_in_house.appointments.model.AvailableSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long> {

    @Query("SELECT s FROM AvailableSlot s WHERE s.slotDate = :date AND s.isAvailable = true ORDER BY s.startTime ASC")
    List<AvailableSlot> findAvailableSlotsByDate(@Param("date") LocalDate date);

    Optional<AvailableSlot> findBySlotDateAndStartTime(LocalDate slotDate, LocalTime startTime);

    boolean existsBySlotDate(LocalDate slotDate);
}
