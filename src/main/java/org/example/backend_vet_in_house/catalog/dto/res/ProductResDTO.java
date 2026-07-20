package org.example.backend_vet_in_house.catalog.dto.res;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.example.backend_vet_in_house.catalog.model.Categoria;

import java.math.BigDecimal;

@JsonPropertyOrder({
        "code",
        "name",
        "description",
        "price",
        "image",
        "categoria"
})
public record ProductResDTO(
        String code,
        String name,
        String description,
        BigDecimal price,
        String image,
        String categoria
) {
}
