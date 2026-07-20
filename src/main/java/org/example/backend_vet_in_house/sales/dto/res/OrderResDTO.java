package org.example.backend_vet_in_house.sales.dto.res;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({
        "code",
        "subtotal",
        "tax",
        "shippingCost",
        "totalAmount",
        "orderStatus",
        "shippingAddress",
        "createAt",
        "updateAt",
        "paidAt",
        "orderDetailDTO"
})
public record OrderResDTO(
        String code,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal shippingCost,
        BigDecimal totalAmount,
        String orderStatus,
        String shippingAddress,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        LocalDateTime paidAt,
        List<OrderDetailResDTO> orderDetailDTO
) {
}
