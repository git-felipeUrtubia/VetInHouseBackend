package org.example.backend_vet_in_house.catalog.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "code")
    private String code;

    private String name;

    private String description;

    private BigDecimal price;

    private String image;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private int stock;
}
