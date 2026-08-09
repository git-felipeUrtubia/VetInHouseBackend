package org.example.backend_vet_in_house.sales.dto.req;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateOrderReqDTO(
        String code,
        String username,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal shippingCost,
        BigDecimal totalAmount,
        String orderStatus,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        LocalDateTime paidAt,
        List<OrderDetailReqDTO> orderDetails,
        AddressReqDTO address,
        String codeCommune,
        String codeRegion
) {
}
