package com.workshop.webhook.service;

import com.workshop.webhook.model.WebhookPayload;
import com.workshop.webhook.model.WebhookHistory;
import com.workshop.webhook.repository.WebhookHistoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebhookService {

    private final WebhookHistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    public WebhookService(WebhookHistoryRepository historyRepository, ObjectMapper objectMapper) {
        this.historyRepository = historyRepository;
        this.objectMapper = objectMapper;
    }

    public void processWebhook(WebhookPayload payload, String sourceIp) {
        WebhookHistory history = new WebhookHistory(payload.eventType(), serializePayload(payload), sourceIp);

        try {
            switch (payload.eventType()) {
                case "payment.succeeded" -> processPayment(payload);
                case "user.registered" -> processUserRegistration(payload);
                case "order.shipped" -> processOrderShipped(payload);
                default -> {
                    history.setProcessedSuccessfully(false);
                    history.setErrorMessage("Unknown event type: " + payload.eventType());
                }
            }
            history.setProcessedSuccessfully(true);
        } catch (Exception e) {
            history.setProcessedSuccessfully(false);
            history.setErrorMessage(e.getMessage());
        }

        historyRepository.save(history);
    }

    private void processPayment(WebhookPayload payload) {
        System.out.println("Processing payment: " + payload.data());
    }

    private void processUserRegistration(WebhookPayload payload) {
        System.out.println("Processing user registration: " + payload.data());
    }

    private void processOrderShipped(WebhookPayload payload) {
        System.out.println("Processing order shipment: " + payload.data());
    }

    private String serializePayload(WebhookPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            return "Error serializing payload: " + e.getMessage();
        }
    }

    public List<WebhookHistory> getWebhookHistory() {
        return historyRepository.findAllByOrderByReceivedAtDesc();
    }
}