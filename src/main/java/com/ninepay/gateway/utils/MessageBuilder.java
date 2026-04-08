package com.ninepay.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninepay.gateway.exceptions.PaymentException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MessageBuilder {
    private String method = "GET";
    private String uri = "";
    private Map<String, String> headers = new TreeMap<>();
    private String date = "";
    private Map<String, String> params = new TreeMap<>();
    private String body = null;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static MessageBuilder instance() {
        return new MessageBuilder();
    }

    public MessageBuilder with(String date, String uri, String method, Map<String, String> headers) {
        this.date = date;
        this.uri = uri;
        this.method = method;
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    public MessageBuilder with(String date, String uri, String method) {
        return with(date, uri, method, null);
    }

    public MessageBuilder withBody(Object body) throws JsonProcessingException {
        if (body instanceof String) {
            this.body = (String) body;
        } else {
            this.body = objectMapper.writeValueAsString(body);
        }
        return this;
    }

    public MessageBuilder withParams(Map<String, String> params) {
        if (params != null) {
            this.params.putAll(params);
        }
        return this;
    }

    public String build() throws PaymentException {
        validate();

        String canonicalHeaders = buildCanonicalString(headers);
        String canonicalPayload;

        if ("POST".equalsIgnoreCase(method) && body != null) {
            canonicalPayload = canonicalBody();
        } else {
            canonicalPayload = buildCanonicalString(params);
        }

        List<String> components = new ArrayList<>();
        components.add(method.toUpperCase());
        components.add(uri);
        components.add(date);
        if (!canonicalHeaders.isEmpty()) {
            components.add(canonicalHeaders);
        }
        if (!canonicalPayload.isEmpty()) {
            components.add(canonicalPayload);
        }

        return String.join("\n", components);
    }

    private void validate() throws PaymentException {
        if (uri == null || uri.isEmpty() || date == null || date.isEmpty()) {
            throw new PaymentException("MessageBuilder: missing uri/date");
        }
    }

    private String buildCanonicalString(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        // TreeMap already sorts by key
        List<String> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                pairs.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // Should not happen with UTF-8
            }
        }
        return String.join("&", pairs);
    }

    private String canonicalBody() {
        if (body == null || body.isEmpty()) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(body.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return build();
        } catch (PaymentException e) {
            return "";
        }
    }
}
