package org.example.backend_vet_in_house.catalog.dto.req;

import org.example.backend_vet_in_house.catalog.model.Categoria;

import java.math.BigDecimal;

public record CreateProductDTO(
        String name,
        String code,
        String description,
        BigDecimal price,
        BigDecimal priceOffer,
        String image,
        String categoria,
        int stock
) {
}
