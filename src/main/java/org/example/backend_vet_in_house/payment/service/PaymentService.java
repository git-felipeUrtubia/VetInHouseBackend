package org.example.backend_vet_in_house.payment.service;

import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCommitResponse;
import cl.transbank.webpay.webpayplus.responses.WebpayPlusTransactionCreateResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    // 1. Credenciales de INTEGRACIÓN (Pruebas) oficiales de Transbank para Webpay Plus
    private final String apiKey = "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C";
    private final String commerceCode = "597055555532";

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


















