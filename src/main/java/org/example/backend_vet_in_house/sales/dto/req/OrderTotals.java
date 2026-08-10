package org.example.backend_vet_in_house.sales.dto.req;

import java.math.BigDecimal;

public record OrderTotals(
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal shippingCost,
        BigDecimal totalAmount
) {
}
