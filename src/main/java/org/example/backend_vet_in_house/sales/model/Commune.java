package org.example.backend_vet_in_house.sales.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "commune")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Commune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commune_id")
    private Long communeId;

    private String code;

    private String commune;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "commune")
    List<Address> address;

    @ManyToOne
    @JoinColumn(name = "region_id")
    Region region;
}
