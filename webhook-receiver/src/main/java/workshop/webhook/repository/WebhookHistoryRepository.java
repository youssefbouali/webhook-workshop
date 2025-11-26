package com.workshop.webhook.repository;

import com.workshop.webhook.model.WebhookHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WebhookHistoryRepository extends JpaRepository<WebhookHistory, Long> {
    List<WebhookHistory> findAllByOrderByReceivedAtDesc();
    List<WebhookHistory> findByEventTypeOrderByReceivedAtDesc(String eventType);
    List<WebhookHistory> findByProcessedSuccessfullyFalse();
}