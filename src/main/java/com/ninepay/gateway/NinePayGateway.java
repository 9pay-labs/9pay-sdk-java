package com.ninepay.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninepay.gateway.config.NinePayConfig;
import com.ninepay.gateway.request.CreatePaymentRequest;
import com.ninepay.gateway.response.PaymentResponse;
import com.ninepay.gateway.response.ResponseInterface;
import com.ninepay.gateway.utils.Environment;
import com.ninepay.gateway.utils.MessageBuilder;
import com.ninepay.gateway.utils.Signature;
import com.ninepay.gateway.request.PayerAuthRequest;
import com.ninepay.gateway.request.AuthorizeRequest;
import com.ninepay.gateway.request.CaptureRequest;
import com.ninepay.gateway.request.ReverseRequest;
import com.ninepay.gateway.response.PayerAuthResponse;
import com.ninepay.gateway.response.AuthorizeResponse;
import com.ninepay.gateway.response.CaptureResponse;
import com.ninepay.gateway.response.ReverseResponse;
import com.ninepay.gateway.utils.SignatureV2Util;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NinePayGateway implements PaymentGatewayInterface {
    private final String clientId;
    private final String secretKey;
    private final String checksumKey;
    private final String endpoint;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NinePayGateway(NinePayConfig config) {
        this.clientId = config.getMerchantId();
        this.secretKey = config.getSecretKey();
        this.checksumKey = config.getChecksumKey();
        this.endpoint = Environment.endpoint(config.getEnv());
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public ResponseInterface createPayment(CreatePaymentRequest paymentRequest) throws Exception {
        String time = String.valueOf(Instant.now().getEpochSecond());
        Map<String, Object> payload = new HashMap<>();
        payload.put("merchantKey", clientId);
        payload.put("time", time);
        payload.putAll(paymentRequest.toPayload());

        // Build message for signature
        Map<String, String> stringParams = payload.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));

        String message = MessageBuilder.instance()
                .with(time, endpoint + "/payments/create", "POST")
                .withParams(stringParams)
                .build();

        String signature = Signature.sign(message, secretKey);

        // Build redirect URL
        String jsonPayload = objectMapper.writeValueAsString(payload);
        String baseEncode = Base64.getEncoder().encodeToString(jsonPayload.getBytes(StandardCharsets.UTF_8));

        String redirectUrl = endpoint + "/portal?" +
                "baseEncode=" + URLEncoder.encode(baseEncode, "UTF-8") +
                "&signature=" + URLEncoder.encode(signature, "UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("redirect_url", redirectUrl);
        return new PaymentResponse(true, data, "OK");
    }

    @Override
    public ResponseInterface inquiry(String transactionId) throws Exception {
        String time = String.valueOf(Instant.now().getEpochSecond());
        String uri = endpoint + "/v2/payments/" + transactionId + "/inquire";

        String message = MessageBuilder.instance()
                .with(time, uri, "GET")
                .build();

        String signature = Signature.sign(message, secretKey);
        String authHeader = "Signature Algorithm=HS256,Credential=" + clientId + ",SignedHeaders=,Signature="
                + signature;

        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Date", time)
                .addHeader("Authorization", authHeader)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            boolean success = response.isSuccessful();
            String body = response.body() != null ? response.body().string() : "{}";
            Map<String, Object> data = objectMapper.readValue(body, Map.class);
            String messageStr = data.containsKey("message") ? String.valueOf(data.get("message")) : "";
            return new PaymentResponse(success, data, messageStr);
        }
    }

    public boolean verify(String result, String checksum) {
        if (result == null || result.isEmpty() || checksum == null || checksum.isEmpty()) {
            return false;
        }

        try {
            String combined = result + checksumKey;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().equalsIgnoreCase(checksum);
        } catch (Exception e) {
            return false;
        }
    }

    public String decodeResult(String result) {
        if (result == null)
            return "";
        try {
            // URL-safe Base64 decode
            return new String(Base64.getUrlDecoder().decode(result.replace('-', '+').replace('_', '/')),
                    StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            // Try standard base64 if url-safe fails
            try {
                return new String(Base64.getDecoder().decode(result), StandardCharsets.UTF_8);
            } catch (Exception ex) {
                return "";
            }
        }
    }

    @Override
    public ResponseInterface payerAuth(PayerAuthRequest request) throws Exception {
        return processRequest(request, "/v2/payments/payer-auth", PayerAuthResponse.class);
    }

    @Override
    public ResponseInterface authorize(AuthorizeRequest request) throws Exception {
        return processRequest(request, "/v2/payments/authorize", AuthorizeResponse.class);
    }

    @Override
    public ResponseInterface capture(CaptureRequest request) throws Exception {
        return processRequest(request, "/v2/payments/capture", CaptureResponse.class);
    }

    @Override
    public ResponseInterface reverse(ReverseRequest request) throws Exception {
        return processRequest(request, "/v2/payments/reverse-auth", ReverseResponse.class);
    }

    private <T extends ResponseInterface> T processRequest(Object requestObj, String path, Class<T> clazz)
            throws Exception {
        String time = String.valueOf(Instant.now().getEpochSecond());
        String uri = endpoint + path;

        String jsonBody = objectMapper.writeValueAsString(requestObj);
        Map<String, Object> verifyParams = new HashMap<>();
        verifyParams.put("json", jsonBody);

        String httpQuery = SignatureV2Util.buildHttpQuery(verifyParams);
        String signature = SignatureV2Util.createSignature("POST", uri, Long.parseLong(time), httpQuery, secretKey);

        String authHeader = "Signature Algorithm=HS256,Credential=" + clientId + ",SignedHeaders=,Signature="
                + signature;

        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));

        Request httpRequest = new Request.Builder()
                .url(uri)
                .addHeader("Date", time)
                .addHeader("Authorization", authHeader)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String bodyStr = response.body() != null ? response.body().string() : "{}";
            Map<String, Object> data = objectMapper.readValue(bodyStr, Map.class);

            boolean success = response.isSuccessful();

            String message = "";
            if (data.containsKey("message")) {
                message = String.valueOf(data.get("message"));
            }

            return clazz.getConstructor(boolean.class, String.class, Map.class).newInstance(success, message, data);
        }
    }
}
