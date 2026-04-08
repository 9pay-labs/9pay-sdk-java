package com.ninepay.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
    INSTALLMENT("INSTALLMENT"),
    CARD_AUTHORIZATION("CARD_AUTHORIZATION");

    private final String value;

    public static boolean isValid(String type) {
        for (TransactionType t : values()) {
            if (t.value.equalsIgnoreCase(type)) return true;
        }
        return false;
    }
}
