package org.example.backend_vet_in_house.appointments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "pet_id_ref")
    private Long petIdRef;

    @Column(name = "code_service")
    private String codeService;

    @Column(name = "reason_for_visit")
    private String reasonForVisit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private Status status;
}
