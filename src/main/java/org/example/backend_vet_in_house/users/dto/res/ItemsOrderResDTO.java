package org.example.backend_vet_in_house.users.dto.res;

import java.math.BigDecimal;

public record ItemsOrderResDTO(
        String nameProduct,
        BigDecimal unitPrice,
        BigDecimal priceOffer,
        int quantity
) {
}
