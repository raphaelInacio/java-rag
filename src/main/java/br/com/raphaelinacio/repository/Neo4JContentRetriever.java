package br.com.raphaelinacio.repository;

import br.com.raphaelinacio.shared.Utils;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.neo4j.Neo4jContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.graph.neo4j.Neo4jGraph;

import java.util.List;
import java.util.stream.Collectors;

public class Neo4JContentRetriever {

  private final Neo4jContentRetriever retriever;
  private final PromptTemplate promptTemplate = PromptTemplate.from("""
      Texto inicial:

      Você é um desenvolvedor Neo4j que traduz perguntas de usuários em Cypher para responder a perguntas sobre uma arquitetura de microsserviço.\s
      Converta a pergunta do usuário em uma instrução Cypher com base no schema.

      O que você deve fazer:

      Utilizar apenas os nós, relacionamentos e propriedades mencionados no schema.
      usar IS NOT NULL para verificar a existência da propriedade, e não a função exists().
      Limitar o número máximo de resultados para 10.
      Responder apenas com uma instrução Cypher. Sem introdução.

      Exemplos de pergunta e resposta Cypher:

       Pergunta de exemplo: Liste o nome e a descrição de todos os microsserviços?
       Exemplo de Cypher:
       MATCH (m:Microservice) RETURN m.name, m.description;
       Use o código com cuidado.

       Pergunta de exemplo: Qual o número total de microsserviços?

       Exemplo de Cypher:
       MATCH (m:Microservice) RETURN COUNT(m) as total;
       Use o código com cuidado.

       Pergunta de exemplo: Qual o número total de bancos de dados?

       Exemplo de Cypher:
       MATCH (database:Database) RETURN COUNT(database) as total;
       Use o código com cuidado.

       Pergunta de exemplo: O que faz o microsserviço UserService?
       Exemplo de Cypher:
       MATCH (m:Microservice) WHERE m.name = 'UserService' RETURN m.description;
       Use o código com cuidado.

       Esquema:
       {{schema}}

       Pergunta:
       {{question}}
    """);

  public Neo4JContentRetriever(ChatLanguageModel chatLanguageModel, Neo4jGraph neo4jGraph) {
    this.retriever = Neo4jContentRetriever.builder()
      .graph(neo4jGraph)
      .chatLanguageModel(chatLanguageModel)
      .promptTemplate(this.promptTemplate)
      .build();
  }

  public String neo4jContentRetriever(String question) {
    List<Content> contents = retriever.retrieve(new Query(question));
    if (!contents.isEmpty()) {
      contents.forEach(content -> content.textSegment().text());
      List<String> collect = contents.stream()
        .map(content -> content.textSegment().text())
        .collect(Collectors.toList());
      return String.join(",", collect);
    }
    return "Não foi possível encontrar a resposta";
  }

  public static void main(String[] args) {
    ChatLanguageModel model = VertexAiGeminiChatModel.builder()
      .project(System.getenv("PROJECT_ID"))
      .location(System.getenv("LOCATION"))
      .modelName("gemini-1.5-flash-001")
      .maxOutputTokens(1000)
      .build();

    Neo4JContentRetriever talkExample = new Neo4JContentRetriever(model, Utils.getNeo4J());
    var question = "Liste todos os Bancos de dados e a quantidade de dependecias com Microservice?";
    var question2 = "Quais Microservices utilizam Queues, informe o nome do Microservice e Queue?";
    var response = talkExample.neo4jContentRetriever(question);
    System.out.println("Pergunta: " + question);
    System.out.println("Resposta: " + response);
  }
}


