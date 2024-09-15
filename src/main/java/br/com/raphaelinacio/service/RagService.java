package br.com.raphaelinacio.service;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface RagService {
  TokenStream ask(String question);
}
