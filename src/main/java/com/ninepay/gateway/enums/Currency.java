package com.ninepay.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    VND("VND"),
    USD("USD"),
    IDR("IDR"),
    EUR("EUR"),
    GBP("GBP"),
    CNY("CNY"),
    JPY("JPY"),
    AUD("AUD"),
    KRW("KRW"),
    CAD("CAD"),
    HKD("HKD"),
    INR("INR");

    private final String value;

    public static boolean isValid(String code) {
        for (Currency c : values()) {
            if (c.value.equalsIgnoreCase(code)) return true;
        }
        return false;
    }
}
