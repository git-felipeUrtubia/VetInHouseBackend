package org.example.backend_vet_in_house.sales.dto.res;

import java.math.BigDecimal;

public record OrderDetailResDTO(
        String productName,
        BigDecimal unitPrice,
        BigDecimal priceOffer,
        int quantity
) {
}
