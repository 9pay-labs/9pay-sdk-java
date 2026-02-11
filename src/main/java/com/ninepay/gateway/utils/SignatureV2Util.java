package com.ninepay.gateway.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SignatureV2Util {

    public static String buildHttpQuery(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        // Sort parameters by key
        TreeMap<String, Object> sortedParams = new TreeMap<>(params);

        return sortedParams.entrySet().stream()
                .map(entry -> {
                    try {
                        String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
                        String value = URLEncoder.encode(String.valueOf(entry.getValue()),
                                StandardCharsets.UTF_8.toString());
                        return key + "=" + value;
                    } catch (UnsupportedEncodingException e) {
                        return "";
                    }
                })
                .collect(Collectors.joining("&"))
                .replace("%20", "+"); // Ensure compatibility with JS encodeURIComponent replace
    }

    public static String createSignature(String method, String url, long timestamp, String httpQuery,
            String secretKey) {
        String message = method + "\n" + url + "\n" + timestamp + "\n" + httpQuery;

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }
}
