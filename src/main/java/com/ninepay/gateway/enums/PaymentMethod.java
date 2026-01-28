package com.ninepay.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    ATM_CARD("ATM_CARD"),
    CREDIT_CARD("CREDIT_CARD"),
    NINE_PAY("9PAY"),
    COLLECTION("COLLECTION"),
    APPLE_PAY("APPLE_PAY"),
    BUY_NOW_PAY_LATER("BUY_NOW_PAY_LATER"),
    QR_PAY("QR_PAY"),
    VNPAY_PORTONE("VNPAY_PORTONE"),
    ZALOPAY_WALLET("ZALOPAY_WALLET"),
    GOOGLE_PAY("GOOGLE_PAY");

    private final String value;

    public static boolean isValid(String method) {
        for (PaymentMethod m : values()) {
            if (m.value.equalsIgnoreCase(method)) return true;
        }
        return false;
    }
}
