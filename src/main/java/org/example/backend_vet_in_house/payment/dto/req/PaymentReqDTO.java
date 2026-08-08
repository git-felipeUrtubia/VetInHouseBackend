package org.example.backend_vet_in_house.payment.dto.req;

public record PaymentReqDTO(
        String buyOrder,
        String sessionId,
        double amount,
        String returnUrl
) {
}
