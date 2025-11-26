package com.workshop.webhook.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WebhookSecurityService {

    private static final String HMAC_SHA256 = "HmacSHA256";

    @Value("${webhook.secret:default-secret-change-in-production}")
    private String webhookSecret;

    public boolean verifySignature(String payload, String receivedSignature) {
        if (!StringUtils.hasText(receivedSignature)) {
            return false;
        }
        String expectedSignature = calculateSignature(payload);
        return expectedSignature.equals(receivedSignature);
    }

    public String calculateSignature(String payload) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(payload.getBytes());
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error calculating signature", e);
        }
    }
}