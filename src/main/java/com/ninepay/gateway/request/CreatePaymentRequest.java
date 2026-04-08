package com.ninepay.gateway.request;

import com.ninepay.gateway.enums.Currency;
import com.ninepay.gateway.enums.Language;
import com.ninepay.gateway.enums.PaymentMethod;
import com.ninepay.gateway.enums.TransactionType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CreatePaymentRequest implements RequestInterface {
    private final String requestCode;
    private final String amount;
    private final String description;
    private String backUrl;
    private String returnUrl;

    private String method;
    private String clientIp;
    private String currency;
    private String lang;
    private String cardToken;
    private Integer saveToken;
    private String transactionType;
    private String clientPhone;
    private Integer expiresTime;
    private final Map<String, Object> extraData = new HashMap<>();

    public CreatePaymentRequest(String requestCode, String amount, String description, String backUrl, String returnUrl) {
        if (requestCode == null || requestCode.isEmpty() || amount == null || amount.isEmpty() || description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Missing required fields: requestCode, amount, description");
        }
        this.requestCode = requestCode;
        this.amount = amount;
        this.description = description;
        this.backUrl = backUrl;
        this.returnUrl = returnUrl;
    }

    public CreatePaymentRequest(String requestCode, String amount, String description) {
        this(requestCode, amount, description, null, null);
    }

    public CreatePaymentRequest withMethod(String method) {
        if (!PaymentMethod.isValid(method)) {
            throw new IllegalArgumentException("Invalid payment method: " + method);
        }
        this.method = method;
        return this;
    }

    public CreatePaymentRequest withMethod(PaymentMethod method) {
        this.method = method.getValue();
        return this;
    }

    public CreatePaymentRequest withClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public CreatePaymentRequest withBackUrl(String backUrl) {
        this.backUrl = backUrl;
        return this;
    }

    public CreatePaymentRequest withReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public CreatePaymentRequest withCurrency(String currency) {
        if (!Currency.isValid(currency)) {
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }
        this.currency = currency;
        return this;
    }

    public CreatePaymentRequest withCurrency(Currency currency) {
        this.currency = currency.getValue();
        return this;
    }

    public CreatePaymentRequest withLang(String lang) {
        if (!Language.isValid(lang)) {
            throw new IllegalArgumentException("Invalid language: " + lang);
        }
        this.lang = lang;
        return this;
    }

    public CreatePaymentRequest withLang(Language lang) {
        this.lang = lang.getValue();
        return this;
    }

    public CreatePaymentRequest withCardToken(String cardToken) {
        this.cardToken = cardToken;
        return this;
    }

    public CreatePaymentRequest withSaveToken(int saveToken) {
        this.saveToken = saveToken;
        return this;
    }

    public CreatePaymentRequest withTransactionType(String transactionType) {
        if (!TransactionType.isValid(transactionType)) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
        this.transactionType = transactionType;
        return this;
    }

    public CreatePaymentRequest withTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType.getValue();
        return this;
    }

    public CreatePaymentRequest withClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
        return this;
    }

    public CreatePaymentRequest withExpiresTime(int expiresTime) {
        if (expiresTime < 0) {
            throw new IllegalArgumentException("Expires time must be positive");
        }
        this.expiresTime = expiresTime;
        return this;
    }

    public CreatePaymentRequest withParam(String key, Object value) {
        this.extraData.put(key, value);
        return this;
    }

    public Map<String, Object> toPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("invoice_no", requestCode);
        payload.put("amount", amount);
        payload.put("description", description);
        if (backUrl != null) payload.put("back_url", backUrl);
        if (returnUrl != null) payload.put("return_url", returnUrl);
        if (method != null) payload.put("method", method);
        if (clientIp != null) payload.put("client_ip", clientIp);
        if (currency != null) payload.put("currency", currency);
        if (lang != null) payload.put("lang", lang);
        if (cardToken != null) payload.put("card_token", cardToken);
        if (saveToken != null) payload.put("save_token", saveToken);
        if (transactionType != null) payload.put("transaction_type", transactionType);
        if (clientPhone != null) payload.put("client_phone", clientPhone);
        if (expiresTime != null) payload.put("expires_time", expiresTime);
        payload.putAll(extraData);
        return payload;
    }
}
