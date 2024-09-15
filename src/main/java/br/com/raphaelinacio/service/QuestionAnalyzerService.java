package br.com.raphaelinacio.service;

import br.com.raphaelinacio.shared.TypeQuestion;
import dev.langchain4j.service.UserMessage;

public interface QuestionAnalyzerService {
    @UserMessage("Analyze the type de question  of the following issue: {{it}}")
    TypeQuestion analyze(String question);
}
