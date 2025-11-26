package com.workshop.webhook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_history")
public class WebhookHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private LocalDateTime receivedAt;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String sourceIp;

    private Boolean processedSuccessfully;
    private String errorMessage;

    public WebhookHistory() {}

    public WebhookHistory(String eventType, String payload, String sourceIp) {
        this.eventType = eventType;
        this.payload = payload;
        this.sourceIp = sourceIp;
        this.receivedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getSourceIp() { return sourceIp; }
    public void setSourceIp(String sourceIp) { this.sourceIp = sourceIp; }
    public Boolean getProcessedSuccessfully() { return processedSuccessfully; }
    public void setProcessedSuccessfully(Boolean processedSuccessfully) { this.processedSuccessfully = processedSuccessfully; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}