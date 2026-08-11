package org.example.backend_vet_in_house.sales.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "orders_detail")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_detail_id")
    private Long ordersDetailId;

    @Column(name = "product_id_ref")
    private Long productIdRef;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "price_offer")
    private BigDecimal priceOffer;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;
}
