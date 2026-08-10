package org.example.backend_vet_in_house.sales.dto.req;

import java.math.BigDecimal;

public record RegionReqDTO(
        String code,
        String region,
        BigDecimal shippingCost
) {
}
