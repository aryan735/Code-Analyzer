package com.ai.codeanalyzer.Ai_Code_Analyzer.Data;

import lombok.Data;

@Data
public class CodeRequest {

    private String codeContent;

    public String getCodeContent() {
        return codeContent;
    }

    public CodeRequest(String codeContent) {
        this.codeContent = codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }
}
