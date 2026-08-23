package org.example.backend_vet_in_house.appointments.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment_result")
public class AppointmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;

    // Opcional: Si decides guardar el PDF en un servicio como AWS S3 o Cloudinary
    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "appointment_id_ref", referencedColumnName = "appointment_id", unique = true)
    private Appointment appointment;
}
