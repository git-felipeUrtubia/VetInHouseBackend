package org.example.backend_vet_in_house.pets.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "user_id_ref")
    private Long userIdRef;

    @Column(name = "patient_number")
    private String patientNumber;

    private String name;

    private int age;

    private double weight;

    @Enumerated(EnumType.STRING)
    private Specie specie;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String breed;

    @Column(name = "is_neutered")
    private boolean isNeutered;

    private String allergies;

    private String microchipNumber;
}
