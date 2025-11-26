package com.workshop.webhook.model;

import java.time.LocalDateTime;
import java.util.Map;

public record WebhookPayload(
    String eventType,
    LocalDateTime timestamp,
    Map<String, Object> data,
    String signature
) {}