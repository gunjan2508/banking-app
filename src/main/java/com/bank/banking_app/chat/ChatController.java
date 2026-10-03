package com.bank.banking_app.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Value("${groq.api.key}")
    private String apiKey;

    @PostMapping("/message")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> systemMsg = Map.of(
                    "role", "system",
                    "content", "You are Vault Assistant for NeoVault Private Banking. Help users with account management, transfers (daily limit ₹1,00,000), loans (Home 8.5%, Car 9.5%, Education 10.5%, Personal 12%), Fixed Deposits (1yr 6.5%, 2yr 7%, 3yr 7.25%, 5yr 7.5%), beneficiaries, and transaction history. Be concise, friendly, professional."
            );

            Map<String, Object> userMsg = Map.of(
                    "role", "user",
                    "content", request.getOrDefault("message", "hello")
            );

            Map<String, Object> body = new java.util.HashMap<>();
            body.put("model", "llama3-8b-8192");
            body.put("max_tokens", 400);
            body.put("messages", List.of(systemMsg, userMsg));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    entity,
                    Map.class
            );

            Map<?, ?> responseBody = response.getBody();
            if (responseBody == null) {
                return ResponseEntity.ok(Map.of("reply", "No response received."));
            }

            Object choicesObj = responseBody.get("choices");
            if (choicesObj instanceof List<?> choices && !choices.isEmpty()) {
                Object firstChoice = choices.get(0);
                if (firstChoice instanceof Map<?, ?> choiceMap) {
                    Object messageObj = choiceMap.get("message");
                    if (messageObj instanceof Map<?, ?> messageMap) {
                        Object content = messageMap.get("content");
                        if (content != null) {
                            return ResponseEntity.ok(Map.of("reply", content.toString()));
                        }
                    }
                }
            }
            return ResponseEntity.ok(Map.of("reply", "Could not process. Please try again."));

        } catch (Exception e) {
            System.out.println("Chat error: " + e.getMessage());
            return ResponseEntity.ok(Map.of("reply", "Sorry, temporarily unavailable."));
        }
    }
}