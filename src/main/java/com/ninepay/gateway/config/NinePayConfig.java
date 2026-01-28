package com.ninepay.gateway.config;

import com.ninepay.gateway.exceptions.InvalidConfigException;
import lombok.Getter;

@Getter
public class NinePayConfig {
    private final String merchantId;
    private final String secretKey;
    private final String checksumKey;
    private final String env;

    public NinePayConfig(String merchantId, String secretKey, String checksumKey, String env) throws InvalidConfigException {
        if (merchantId == null || merchantId.isEmpty() ||
            secretKey == null || secretKey.isEmpty() ||
            checksumKey == null || checksumKey.isEmpty()) {
            throw new InvalidConfigException("NinePay config requires merchantId, secretKey, checksumKey");
        }
        this.merchantId = merchantId;
        this.secretKey = secretKey;
        this.checksumKey = checksumKey;
        this.env = (env == null || env.isEmpty()) ? "SANDBOX" : env;
    }

    public NinePayConfig(String merchantId, String secretKey, String checksumKey) throws InvalidConfigException {
        this(merchantId, secretKey, checksumKey, "SANDBOX");
    }
}
