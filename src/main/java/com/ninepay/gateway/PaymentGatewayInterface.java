package com.ninepay.gateway;

import com.ninepay.gateway.request.CreatePaymentRequest;
import com.ninepay.gateway.response.ResponseInterface;

public interface PaymentGatewayInterface {
    ResponseInterface createPayment(CreatePaymentRequest request) throws Exception;

    ResponseInterface inquiry(String transactionId) throws Exception;

    ResponseInterface payerAuth(com.ninepay.gateway.request.PayerAuthRequest request) throws Exception;

    ResponseInterface authorize(com.ninepay.gateway.request.AuthorizeRequest request) throws Exception;

    ResponseInterface capture(com.ninepay.gateway.request.CaptureRequest request) throws Exception;

    ResponseInterface reverse(com.ninepay.gateway.request.ReverseRequest request) throws Exception;
}
