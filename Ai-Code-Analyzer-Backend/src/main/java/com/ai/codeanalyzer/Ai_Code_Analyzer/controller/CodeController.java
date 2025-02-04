package com.ai.codeanalyzer.Ai_Code_Analyzer.controller;


import com.ai.codeanalyzer.Ai_Code_Analyzer.Data.CodeRequest;
import com.ai.codeanalyzer.Ai_Code_Analyzer.services.CodeServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")

@AllArgsConstructor
public class CodeController {

    @Autowired
    private CodeServices codeServices;


    @PostMapping("/analyze-code")
    public ResponseEntity<String> getResponse(@RequestBody CodeRequest code){
        String  response =  codeServices.getCodeResponse(code);
        return ResponseEntity.ok(response);
    }


}
