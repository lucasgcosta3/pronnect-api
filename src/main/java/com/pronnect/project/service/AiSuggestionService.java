package com.pronnect.project.service;

import com.pronnect.project.dto.AiSuggestionRequest;
import com.pronnect.project.dto.AiSuggestionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class AiSuggestionService {

    private final String geminiApiKey;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public AiSuggestionService(
            @Value("${gemini.api-key:}") String geminiApiKey,
            ObjectMapper objectMapper
    ) {
        this.geminiApiKey = geminiApiKey;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public AiSuggestionResponse suggest(AiSuggestionRequest request) {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            log.warn("Gemini API key not configured – returning mock suggestions");
            return mockSuggestion(request.briefDescription());
        }

        try {
            return callGemini(request.briefDescription());
        } catch (Exception e) {
            log.error("Gemini API call failed, falling back to mock", e);
            return mockSuggestion(request.briefDescription());
        }
    }

    private AiSuggestionResponse callGemini(String briefDescription) throws Exception {

        String prompt = """
                Você é um assistente de uma plataforma freelancer chamada Pronnect.
                Uma empresa quer publicar um projeto e forneceu a seguinte descrição breve:
                
                "%s"
                
                Com base nisso, gere uma sugestão em JSON com os seguintes campos:
                - "title": título do projeto (curto e objetivo)
                - "description": descrição detalhada do projeto (2-3 parágrafos)
                - "skills": array de habilidades necessárias (ex: ["Java", "Spring Boot", "PostgreSQL"])
                - "budgetMin": orçamento mínimo sugerido em reais (número)
                - "budgetMax": orçamento máximo sugerido em reais (número)
                - "paymentType": "FIXED_PRICE" ou "HOURLY"
                - "paymentTypeJustification": justificativa breve para a modalidade sugerida
                """.formatted(briefDescription);

        String requestBody = objectMapper.writeValueAsString(new GeminiRequest(prompt, new GenerationConfig("application/json")));

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API returned status " + response.statusCode() + ": " + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());
        String text = root
                .path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();

        return objectMapper.readValue(text, AiSuggestionResponse.class);
    }

    private AiSuggestionResponse mockSuggestion(String briefDescription) {
        String lower = briefDescription.toLowerCase();

        List<String> skills;
        BigDecimal budgetMin;
        BigDecimal budgetMax;
        String paymentType;

        String description;

        if (lower.contains("pet") || lower.contains("pets")) {
            skills = List.of("React Native", "TypeScript", "Firebase", "UI/UX", "REST APIs");
            budgetMin = new BigDecimal("5000");
            budgetMax = new BigDecimal("10000");
            paymentType = "FIXED_PRICE";

            description = "Desenvolvimento de uma plataforma para gerenciamento de pets, permitindo cadastro de animais, controle de vacinas, histórico veterinário, agendamento de consultas e acompanhamento das informações dos pets de forma simples e organizada.";
        }
        else if (lower.contains("api") || lower.contains("backend") || lower.contains("java")) {
            skills = List.of("Java", "Spring Boot", "PostgreSQL", "Docker", "APIs REST");
            budgetMin = new BigDecimal("3000");
            budgetMax = new BigDecimal("5000");
            paymentType = "FIXED_PRICE";

            description = "Desenvolvimento de uma API robusta para atender às necessidades do negócio, com foco em performance, segurança e escalabilidade. O projeto inclui modelagem de banco de dados, integração entre sistemas, documentação técnica e testes.";
        }
        else if (lower.contains("frontend") || lower.contains("react") || lower.contains("site")) {
            skills = List.of("React", "TypeScript", "Next.js", "Tailwind CSS", "Figma");
            budgetMin = new BigDecimal("2500");
            budgetMax = new BigDecimal("4500");
            paymentType = "FIXED_PRICE";

            description = "Criação de uma interface web moderna e responsiva, proporcionando uma experiência intuitiva para os usuários. O projeto inclui desenvolvimento das telas, integração com APIs e adaptação para dispositivos móveis.";
        }
        else {
            skills = List.of("Comunicação", "Gestão de Projetos", "Análise de Requisitos");
            budgetMin = new BigDecimal("2000");
            budgetMax = new BigDecimal("4000");
            paymentType = "HOURLY";

            description = "Projeto personalizado baseado nos requisitos fornecidos, envolvendo levantamento de necessidades, definição de escopo e implementação das funcionalidades necessárias para atender aos objetivos do negócio.";
        }

        return new AiSuggestionResponse(
                " " + capitalizeFirst(briefDescription.length() > 60
                        ? briefDescription.substring(0, 60) + "..."
                        : briefDescription),
                description,
                skills,
                budgetMin,
                budgetMax,
                paymentType,
                paymentType.equals("FIXED_PRICE")
                        ? "Para este tipo de projeto, o pagamento por valor fechado é mais indicado pois o escopo é bem definido e permite ao profissional planejar a entrega completa."
                        : "Para este tipo de projeto, o pagamento por hora é mais indicado pois o escopo pode evoluir ao longo do tempo, dando flexibilidade para ajustes."
        );
    }

    private String capitalizeFirst(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // Inner classes for Gemini API request format
    record GeminiRequest(List<Content> contents, GenerationConfig generationConfig) {
        GeminiRequest(String prompt, GenerationConfig generationConfig) {
            this(List.of(new Content(List.of(new Part(prompt)))), generationConfig);
        }
    }

    record GenerationConfig(String responseMimeType) {}
    record Content(List<Part> parts) {}
    record Part(String text) {}
}
