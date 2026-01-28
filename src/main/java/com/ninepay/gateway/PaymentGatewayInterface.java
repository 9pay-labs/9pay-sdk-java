package com.ninepay.gateway;

import com.ninepay.gateway.request.CreatePaymentRequest;
import com.ninepay.gateway.response.ResponseInterface;

public interface PaymentGatewayInterface {
    ResponseInterface createPayment(CreatePaymentRequest request) throws Exception;
    ResponseInterface inquiry(String transactionId) throws Exception;
}
