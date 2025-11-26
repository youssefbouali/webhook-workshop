package com.workshop.webhook.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class WebhookSecurityService {

    private static final String ALGO = "HmacSHA256";

    @Value("${webhook.secret:my-super-secret-key-12345}")
    private String secret;

    private Mac mac;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, InvalidKeyException {
        mac = Mac.getInstance(ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGO);
        mac.init(keySpec);
    }

    public String calculateSignature(String payloadJson) {
        byte[] bytes = mac.doFinal(payloadJson.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
    }
}