package com.workshop.webhook.controller;

import com.workshop.webhook.model.WebhookPayload;
import com.workshop.webhook.service.WebhookSecurityService;
import com.workshop.webhook.service.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private final WebhookService webhookService;
    private final WebhookSecurityService securityService;

    public WebhookController(WebhookService webhookService, WebhookSecurityService securityService) {
        this.webhookService = webhookService;
        this.securityService = securityService;
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveWebhook(
            @Valid @RequestBody WebhookPayload payload,
            @RequestHeader("X-Webhook-Signature") String signature,
            HttpServletRequest request) {

        String rawPayload = payload.toString(); // Attention : en prod, lire le body brut via @RequestBody String body
        if (!securityService.verifySignature(rawPayload, signature)) {
            return ResponseEntity.status(401).body("Invalid signature");
        }

        CompletableFuture.runAsync(() ->
                webhookService.processWebhook(payload, request.getRemoteAddr()));

        return ResponseEntity.accepted().body("Webhook received and processing");
    }

    @GetMapping("/history")
    public ResponseEntity<?> getWebhookHistory() {
        return ResponseEntity.ok(webhookService.getWebhookHistory());
    }
}