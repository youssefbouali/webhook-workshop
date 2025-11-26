package com.workshop.sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.workshop.sender", "com.workshop.webhook"})
public class WebhookSenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebhookSenderApplication.class, args);
    }
}