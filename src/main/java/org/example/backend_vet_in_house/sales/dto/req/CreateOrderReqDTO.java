package org.example.backend_vet_in_house.sales.dto.req;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderReqDTO(
        String code,
        Long userIdRef,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal shippingCost,
        BigDecimal totalAmount,
        String orderStatus,
        String shippingAddress,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        LocalDateTime paidAt,
        List<OrderDetailReqDTO> orderDetails
) {
}
