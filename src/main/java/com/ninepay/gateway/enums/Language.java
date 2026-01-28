package com.ninepay.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
    VI("vi"),
    EN("en");

    private final String value;

    public static boolean isValid(String lang) {
        for (Language l : values()) {
            if (l.value.equalsIgnoreCase(lang)) return true;
        }
        return false;
    }
}
