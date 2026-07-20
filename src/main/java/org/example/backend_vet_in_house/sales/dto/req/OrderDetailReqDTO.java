package org.example.backend_vet_in_house.sales.dto.req;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetailReqDTO(
    String codeProduct,
    int quantity
) {
}
