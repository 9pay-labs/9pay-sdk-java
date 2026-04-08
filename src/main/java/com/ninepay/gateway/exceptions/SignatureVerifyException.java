package com.ninepay.gateway.exceptions;

public class SignatureVerifyException extends PaymentException {
    public SignatureVerifyException(String message) {
        super(message);
    }
}
