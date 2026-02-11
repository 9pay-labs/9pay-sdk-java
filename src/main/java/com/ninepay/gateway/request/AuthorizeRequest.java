package com.ninepay.gateway.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeRequest implements RequestInterface {
    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("order_code")
    private String orderCode;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("card")
    private CardInfo card;

    @Override
    public Map<String, Object> toPayload() {
        return new HashMap<>();
    }
}
