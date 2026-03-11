# 9PAY Payment Gateway Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/io.github.9pay-labs/ninepay-sdk)](https://central.sonatype.com/artifact/io.github.9pay-labs/ninepay-sdk)
[![Java](https://img.shields.io/badge/Java-11%2B-blue)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

Official Java SDK for integrating **9PAY Payment Gateway**.

This package allows you to:
- Create payment requests with strictly typed parameters.
- Query transaction status.
- Verify webhook / callback data.

---

## Requirements

- Java **11** or higher
- Dependencies:
    - Jackson Databind
    - OkHttp
    - Lombok

---

## Installation

### Maven

```xml
<dependency>
  <groupId>io.github.9pay-labs</groupId>
  <artifactId>ninepay-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.9pay-labs:ninepay-sdk:1.0.0'
```

---

## Configuration

```java
import com.ninepay.gateway.config.NinePayConfig;

NinePayConfig config = new NinePayConfig(
    "YOUR_MERCHANT_ID",
    "YOUR_SECRET_KEY",
    "YOUR_CHECKSUM_KEY",
    "SANDBOX" // or "PRODUCTION"
);
```

---

## Usage

### Initialization

```java
import com.ninepay.gateway.NinePayGateway;

NinePayGateway gateway = new NinePayGateway(config);
```

### Create Payment

```java
import com.ninepay.gateway.request.CreatePaymentRequest;
import com.ninepay.gateway.response.ResponseInterface;
import com.ninepay.gateway.enums.PaymentMethod;

CreatePaymentRequest request = new CreatePaymentRequest(
    "INV_" + System.currentTimeMillis(), // Invoice No
    "50000",                             // Amount
    "Payment for Order 1",              // Description
    "https://site.com/callback",        // Back URL
    "https://site.com/return"           // Return URL
);

request.withMethod(PaymentMethod.ATM_CARD)
       .withClientIp("127.0.0.1");

ResponseInterface response = gateway.createPayment(request);

if (response.isSuccess()) {
    String redirectUrl = (String) response.getData().get("redirect_url");
    // Redirect user to redirectUrl
} else {
    System.out.println("Error: " + response.getMessage());
}
```

### Query Transaction

```java
ResponseInterface response = gateway.inquiry("INV_123456");

if (response.isSuccess()) {
    System.out.println(response.getData());
} else {
    System.out.println(response.getMessage());
}
```

### Verify Webhook / Callback

```java
String result = request.getParameter("result");
String checksum = request.getParameter("checksum");

if (gateway.verify(result, checksum)) {
    String decodedResult = gateway.decodeResult(result);
    // Process JSON decodedResult...
}
```

---

## License

MIT License © 9Pay
