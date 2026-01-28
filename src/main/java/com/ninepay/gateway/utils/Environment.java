package com.ninepay.gateway.utils;

public final class Environment {
    public static final String SAND = "https://sand-payment.9pay.vn";
    public static final String PROD = "https://payment.9pay.vn";

    public static String endpoint(String env) {
        if ("PRODUCTION".equalsIgnoreCase(env)) {
            return PROD;
        }
        return SAND;
    }
}
