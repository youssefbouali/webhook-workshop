package com.workshop.sender.controller;

import com.workshop.sender.service.WebhookSenderService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/test")
public class TestController {

    private final WebhookSenderService webhookSenderService;

    public TestController(WebhookSenderService webhookSenderService) {
        this.webhookSenderService = webhookSenderService;
    }

    @PostMapping("/send-payment")
    public String sendPaymentWebhook() {
        Map<String, Object> paymentData = Map.of(
            "payment_id", "pay_123456",
            "amount", 99.99,
            "currency", "DH",
            "customer_id", "cust_789",
            "status", "succeeded"
        );

        CompletableFuture<Boolean> result = webhookSenderService.sendWebhook("payment.succeeded", paymentData);
        return "Payment webhook sent! Result: " + result.join();
    }

    @PostMapping("/send-user-registration")
    public String sendUserRegistrationWebhook() {
        Map<String, Object> userData = Map.of(
            "user_id", "user_456",
            "email", "Said.Ben@example.com",
            "name", "Said Ben",
            "registration_date", "2024-01-15T10:30:00"
        );

        CompletableFuture<Boolean> result = webhookSenderService.sendWebhook("user.registered", userData);
        return "User registration webhook sent! Result: " + result.join();
    }

    @PostMapping("/send-custom")
    public String sendCustomWebhook(@RequestParam String eventType, @RequestBody Map<String, Object> data) {
        CompletableFuture<Boolean> result = webhookSenderService.sendWebhook(eventType, data);
        return "Custom webhook sent! Result: " + result.join();
    }
}