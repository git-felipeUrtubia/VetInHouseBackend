package org.example.backend_vet_in_house.payment.controller;

import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCommitResponse;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCreateResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.payment.dto.req.PaymentReqDTO;
import org.example.backend_vet_in_house.payment.service.PaymentService;
import org.example.backend_vet_in_house.sales.service.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrdersService ordersService;


    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentReqDTO req) {
        try {
            WebpayPlusTransactionCreateResponse response = paymentService.iniciarPago(
                    req.buyOrder(),
                    req.sessionId(),
                    req.amount(),
                    req.returnUrl()
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar el pago: " + e.getMessage());
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestParam String token) {
        try {
            WebpayPlusTransactionCommitResponse response = paymentService.confirmarPago(token);

            // Aquí en el futuro puedes hacer un if(response.getResponseCode() == 0)
            // para actualizar tu tabla Orders y cambiar el OrderStatus a PAID.

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al confirmar el pago: " + e.getMessage());
        }
    }

}
