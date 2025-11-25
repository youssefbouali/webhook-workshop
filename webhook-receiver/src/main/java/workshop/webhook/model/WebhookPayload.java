package com.workshop.webhook.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

public record WebhookPayload(
    @NotBlank String eventType,
    @NotNull LocalDateTime timestamp,
    Map<String, Object> data,
    String signature) {}