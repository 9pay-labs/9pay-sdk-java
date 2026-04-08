package com.ninepay.gateway.request;

import java.util.Map;

public interface RequestInterface {
    Map<String, Object> toPayload();
}
