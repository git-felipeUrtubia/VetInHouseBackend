package org.example.backend_vet_in_house.users.dto.res;

import org.example.backend_vet_in_house.sales.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record ContentOrderResDTO(
        String code,
        String status,
        BigDecimal subtotal,
        BigDecimal shippingCost,
        BigDecimal tax,
        BigDecimal amount,
        List<ItemsOrderResDTO> orderDetails
) {
}
