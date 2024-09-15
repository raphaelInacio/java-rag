package br.com.raphaelinacio.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ResponseGenerationService {
  @SystemMessage("""
    Sua função:
    Sua função é complementar a resposta relacionada a Question e Response abaixo.
    Sua função é deixar o conteudo suave e entendivel

    Como voce deve responder:
    Sempre de preferencia por respostas sumarizadas ao invés de consolidar a resposta em uma unica frase.
    A resposta de ser em formatação Markdown, aproveite o estilo para destacar palavras importantes e relavantes a resposta
    Ao final sempre inclua uma pequena conclusão com base nos dados enviados 

    Em caso de falhas ou falta de informação:
    Caso a informação do campo Response seja: Não foi possível encontrar a resposta
    Apenas responda que não consegue ajuda-lo naquele momento
    Caso a informação do campo Response venha sem conteudo:
    Apenas responda que não consegue ajuda-lo naquele momento
    """)
  @UserMessage("""
      Question:
    {{question}}

    Response:
    {{response}}
    """)
  TokenStream ask(@V("question") String question, @V("response") String response);
}
