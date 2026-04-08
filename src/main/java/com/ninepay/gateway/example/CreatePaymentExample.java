package com.ninepay.gateway.example;

import com.ninepay.gateway.NinePayGateway;
import com.ninepay.gateway.config.NinePayConfig;
import com.ninepay.gateway.enums.PaymentMethod;
import com.ninepay.gateway.request.CreatePaymentRequest;
import com.ninepay.gateway.response.ResponseInterface;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Scanner;

public class CreatePaymentExample {
    public static void main(String[] args) {
        try {
            // 1. Load configuration from .env
            Dotenv dotenv = Dotenv.load();
            
            String merchantId = dotenv.get("NINEPAY_MERCHANT_ID");
            String secretKey = dotenv.get("NINEPAY_SECRET_KEY");
            String checksumKey = dotenv.get("NINEPAY_CHECKSUM_KEY");
            String env = dotenv.get("NINEPAY_ENV", "SANDBOX");

            if (merchantId == null || secretKey == null || checksumKey == null) {
                System.err.println("Please configure NINEPAY_MERCHANT_ID, NINEPAY_SECRET_KEY, NINEPAY_CHECKSUM_KEY in .env file");
                return;
            }

            // 2. Initialize Gateway
            NinePayConfig config = new NinePayConfig(merchantId, secretKey, checksumKey, env);
            NinePayGateway gateway = new NinePayGateway(config);

            // 3. Create Request
            String invoiceNo = "INV_" + System.currentTimeMillis();
            CreatePaymentRequest request = new CreatePaymentRequest(
                    invoiceNo,
                    "50000",                // Amount
                    "Test Payment Java SDK", // Description
                    "https://your-site.com/back",
                    "https://your-site.com/return"
            );

            // Optional settings
            request.withMethod(PaymentMethod.ATM_CARD)
                   .withClientIp("127.0.0.1");

            // 4. Send Request
            System.out.println("Creating payment for invoice: " + invoiceNo + "...");
            ResponseInterface response = gateway.createPayment(request);

            if (response.isSuccess()) {
                String redirectUrl = (String) response.getData().get("redirect_url");
                System.out.println("========================================");
                System.out.println("Payment Created Successfully!");
                System.out.println("Redirect URL: " + redirectUrl);
                System.out.println("========================================");
                System.out.println("\nPlease open the link above in your browser to complete payment.");
            } else {
                System.err.println("Failed to create payment: " + response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
