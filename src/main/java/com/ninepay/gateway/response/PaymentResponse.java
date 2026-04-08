package com.ninepay.gateway.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class PaymentResponse implements ResponseInterface {
    private final boolean success;
    private final Map<String, Object> data;
    private final String message;

    @Override
    public boolean isSuccess() {
        return success;
    }
}
