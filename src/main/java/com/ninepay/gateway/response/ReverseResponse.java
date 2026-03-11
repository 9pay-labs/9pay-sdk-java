package com.ninepay.gateway.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
public class ReverseResponse implements ResponseInterface {
    private final boolean success;
    private final String message;
    private final Map<String, Object> data;

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
