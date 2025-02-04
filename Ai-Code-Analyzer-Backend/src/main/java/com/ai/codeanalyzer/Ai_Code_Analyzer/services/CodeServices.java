package com.ai.codeanalyzer.Ai_Code_Analyzer.services;

import com.ai.codeanalyzer.Ai_Code_Analyzer.Data.CodeRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class CodeServices {

    @Value("${gemini.url}")
    private String geminiApiUrl;

    @Value("${gemini.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    public CodeServices(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getCodeResponse(CodeRequest codeRequest) {
        if(codeRequest.getCodeContent()==null){
            return "Please provide the code";
        }
        System.out.println("Received code: " + codeRequest.getCodeContent());
        String prompt = buildPrompt(codeRequest);
        Map<String,Object> requestBody=Map.of(
                "contents", new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)  // URL concatenation for the API key
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
         return extractResponseContent(response);

    }

    private  String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
          return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                  .asText().replaceAll("\\*", "") ; // Remove only '*' characters


        } catch (Exception e) {
            return  "Error Processing :"+e.getMessage();
            }
     }


    private String buildPrompt(CodeRequest codeRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Analyze the following code and categorize  the findings into bugs, optimizations, best practices and security issues and also write in sections and use this ✤ symbol for headpoints .  and use ➤ symbol to represent bullet points in professional valid format,and also provide the correct code in format please don't generate a subject line:\n\n" +
                codeRequest.getCodeContent());
        return prompt.toString();
    }






}
