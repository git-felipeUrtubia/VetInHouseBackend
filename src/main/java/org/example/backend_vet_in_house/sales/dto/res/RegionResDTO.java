package org.example.backend_vet_in_house.sales.dto.res;

import java.math.BigDecimal;

public record RegionResDTO(
        String code,
        String region,
        BigDecimal shippingCost
) {
}
