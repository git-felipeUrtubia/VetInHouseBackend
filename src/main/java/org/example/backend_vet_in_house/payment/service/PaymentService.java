package org.example.backend_vet_in_house.payment.service;

import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCommitResponse;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCreateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    // 1. Credenciales de INTEGRACIÓN (Pruebas) oficiales de Transbank para Webpay Plus

    @Value("${transbank.api.key}")
    private String apiKey;

    @Value("${transbank.commerce.code}")
    private String commerceCode;

    public WebpayPlusTransactionCreateResponse iniciarPago(
            String buyOrder,
            String sessionId,
            double amount,
            String returnUrl
    )  throws Exception {

        WebpayPlus.Transaction tx = new WebpayPlus.Transaction(
                new WebpayOptions(commerceCode, apiKey, IntegrationType.TEST)
        );

        return tx.create(
                buyOrder,
                sessionId,
                amount,
                returnUrl
        );
    }

    public WebpayPlusTransactionCommitResponse confirmarPago(String token) throws Exception {
        WebpayPlus.Transaction tx = new WebpayPlus.Transaction(
                new WebpayOptions(commerceCode, apiKey, IntegrationType.TEST)
        );

        // Transbank verifica el token y nos devuelve el estado real del pago
        WebpayPlusTransactionCommitResponse response = tx.commit(token);

        return response;
    }

}


















