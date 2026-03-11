package com.ninepay.gateway.response;

import java.util.Map;

public interface ResponseInterface {
    boolean isSuccess();
    Map<String, Object> getData();
    String getMessage();
}
