package com.research.assistant.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.assistant.model.Geminiresponse;
import com.research.assistant.dto.ResearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class ResearchService {

    @Value("${gemini.api.url}")
    private String geminiApiurl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest researchRequest) {
        String prompt = CraftPrompt(researchRequest);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String response = webClient.post()
                .uri(geminiApiurl + geminiApiKey)
                .headers(headers -> headers.setContentType(MediaType.APPLICATION_JSON))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> new String(body.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8))
                .block();

        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try {
            Geminiresponse geminiresponse = objectMapper.readValue(response, Geminiresponse.class);
            if (geminiresponse.getCandidates() != null && !geminiresponse.getCandidates().isEmpty()) {
                var candidate = geminiresponse.getCandidates().get(0);
                if (candidate.getContent() != null &&
                        candidate.getContent().getParts() != null &&
                        !candidate.getContent().getParts().isEmpty()) {
                    return candidate.getContent().getParts().get(0).getText();
                }
            }
            return "No Content Found in response";
        } catch (Exception e) {
            return "Error parsing: " + e.getMessage();
        }
    }

    private String CraftPrompt(ResearchRequest request) {
        if (request == null || request.getOperations() == null || request.getContent() == null) {
            throw new IllegalArgumentException("Request, operation, and content must not be null");
        }

        StringBuilder prompt = new StringBuilder();
        String operation = request.getOperations();

        switch (operation) {
            case "summarize":
                prompt.append("Provide a clear and concise summary of the following text in a few sentences:\n\n");
                break;
            case "suggest":
                prompt.append("Based on the following content; suggest related topics and further reading. Format the response with clear heading and bullet points:\n\n");
                break;
            case "explain":
                prompt.append("Explain the following content in simple terms, as if to a beginner:\n\n");
                break;
            case "define":
                prompt.append("Provide definitions for key terms and concepts found in the following content:\n\n");
                break;
            case "highlight":
                prompt.append("Highlight the key points and main ideas from the following text:\n\n");
                break;
            case "rephrase":
                prompt.append("Rephrase the following content to make it more readable and engaging:\n\n");
                break;
            case "extract":
                prompt.append("Extract all important facts, data, or statistics from the following text:\n\n");
                break;
            case "related":
                prompt.append("List related topics, concepts, or questions based on the following content:\n\n");
                break;
            case "analyze":
                prompt.append("Analyze the following content and identify the author's intent, tone, and argument:\n\n");
                break;
            case "citation":
                prompt.append("Generate a proper citation in APA format for the following content:\n\n");
                break;
            default:
                throw new IllegalArgumentException("Unknown Operation: " + operation);
        }

        prompt.append(request.getContent());
        return prompt.toString();
    }
}
