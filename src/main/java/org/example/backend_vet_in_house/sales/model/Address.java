package org.example.backend_vet_in_house.sales.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    private String code;

    private String street;

    private String number;

    @OneToOne(mappedBy = "address")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "commune_id")
    Commune commune;

}
