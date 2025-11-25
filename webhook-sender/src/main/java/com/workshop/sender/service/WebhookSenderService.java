package com.workshop.sender.service;

import com.workshop.webhook.model.WebhookPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class WebhookSenderService {

    private final RestTemplate restTemplate;
    private final WebhookSecurityService securityService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${webhook.receiver.url:http://localhost:8081/webhooks/receive}")
    private String webhookReceiverUrl;

    public WebhookSenderService(RestTemplate restTemplate, WebhookSecurityService securityService) {
        this.restTemplate = restTemplate;
        this.securityService = securityService;
    }

    public CompletableFuture<Boolean> sendWebhook(String eventType, Map<String, Object> data) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                WebhookPayload payload = new WebhookPayload(eventType, LocalDateTime.now(), data, null);
                String payloadJson = objectMapper.writeValueAsString(payload);
                String signature = securityService.calculateSignature(payloadJson);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Webhook-Signature", signature);

                HttpEntity<WebhookPayload> request = new HttpEntity<>(payload, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                    webhookReceiverUrl, HttpMethod.POST, request, String.class);

                return response.getStatusCode().is2xxSuccessful();
            } catch (Exception e) {
                System.err.println("Error sending webhook: " + e.getMessage());
                return false;
            }
        });
    }
}