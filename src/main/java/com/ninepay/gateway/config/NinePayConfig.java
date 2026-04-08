package com.ninepay.gateway.config;

import com.ninepay.gateway.exceptions.InvalidConfigException;
import lombok.Getter;

@Getter
public class NinePayConfig {
    private final String merchantId;
    private final String secretKey;
    private final String checksumKey;
    private final String gatewayUrl;

    public NinePayConfig(String merchantId, String secretKey, String checksumKey, String gatewayUrl) throws InvalidConfigException {
        if (merchantId == null || merchantId.isEmpty() ||
            secretKey == null || secretKey.isEmpty() ||
            checksumKey == null || checksumKey.isEmpty() ||
            gatewayUrl == null || gatewayUrl.isEmpty()) {
            throw new InvalidConfigException("NinePay config requires merchantId, secretKey, checksumKey, gatewayUrl");
        }
        this.merchantId = merchantId;
        this.secretKey = secretKey;
        this.checksumKey = checksumKey;
        this.gatewayUrl = gatewayUrl;
    }
}
