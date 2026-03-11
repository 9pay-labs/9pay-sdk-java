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
public class PayerAuthRequest implements RequestInterface {
    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("off_3ds")
    private int off3ds; // 0 or 1

    @JsonProperty("card")
    private CardInfo card;

    @JsonProperty("return_url")
    private String returnUrl;

    @Override
    public Map<String, Object> toPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("request_id", requestId);
        payload.put("currency", currency);
        // Using String value for double to preserve precision if needed?
        // But JSON serializer handles double. Java Map value object.
        payload.put("amount", amount);
        payload.put("off_3ds", off3ds);
        // Note: 'card' is an object. SignatureV2Util needs check how to handle nested
        // objects.
        // Wait, signature V2 uses JSON string for body? Or query params?
        // Postman script:
        // var parameters = { "json" : pm.request.body.raw };
        // var httpQuery = buildHttpQuery(parameters);
        // This means signature V2 signs the WHOLE RAW BODY as 'json' parameter???
        // Wait...
        // "var parameters = { \"json\" : pm.request.body.raw };"
        // So httpQuery string becomes "json=RAW_JSON_STRING".
        // And signature = HMAC(POST + \n + URL + \n + time + \n +
        // "json=RAW_JSON_STRING").

        // This is confusing if not standard.
        // Let's re-read the Postman script carefully.

        return payload;
    }
}
