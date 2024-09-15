package br.com.raphaelinacio.service;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface RagService {
//  @UserMessage("""
//    Voce é um especialista arquitetura
//    Sua função é complementar a resposta relacionada a pergunta e resposta abaixo.
//    A pergunta e resposta são derivadas de um sistema de IA então pode ser que a resposta sejá fria e direta
//    Sua função é deixar o conteudo humanizado e menos direto
//    Não é necessário inventar respostas apenas escrever um texto de forma que a pergunta e resposta façam sentido em uma frase
//    """)
  TokenStream ask(String question);
}
