package br.com.raphaelinacio;

import br.com.raphaelinacio.service.QuestionAnalyzerService;
import br.com.raphaelinacio.service.RagService;
import br.com.raphaelinacio.service.ResponseGenerationService;
import br.com.raphaelinacio.shared.TypeQuestion;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import br.com.raphaelinacio.repository.Neo4JContentRetriever;
import br.com.raphaelinacio.shared.Utils;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Optional;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        var staticPath = Optional.ofNullable(System.getenv("STATIC_PATH")).orElse("/*");
        var httpPort = Optional.ofNullable(System.getenv("HTTP_PORT")).orElse("8888");

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        // Serving static resources
        var staticHandler = StaticHandler.create();
        staticHandler.setCachingEnabled(false);
        router.route(staticPath).handler(staticHandler);

        // Create an HTTP server
        var server = vertx.createHttpServer();

        //! Start the HTTP server
        server.requestHandler(router).listen(Integer.parseInt(httpPort), http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("GenAI Vert-x server started on port " + httpPort);
            } else {
                startPromise.fail(http.cause());
            }
        });

        router.post("/prompt").handler(ctx -> {

            var question = ctx.body().asJsonObject().getString("question");
            HttpServerResponse response = ctx.response();

            response
                    .putHeader("Content-Type", "application/octet-stream")
                    .setChunked(true);

            var streamingModel = getAiGeminiStreamingChatModel();
            var model = getVertexAiGeminiChatModel();

            QuestionAnalyzerService questionAnalyzer = AiServices.create(QuestionAnalyzerService.class, getOpenAiChatModel());
            TypeQuestion analyzeTypeQuestion = questionAnalyzer.analyze(question);

            if (TypeQuestion.DOCUMENTATION.equals(analyzeTypeQuestion)) {

                System.out.println("..: Fazendo uma consulta RAG");

                ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(getAiGeminiEmbeddingStire())
                        .embeddingModel(getAiGeminiEmbeddingModel())
                        .maxResults(1)
                        .build();

                RagService ragService = AiServices.builder(RagService.class)
                        .streamingChatLanguageModel(getAiGeminiStreamingChatModel())
                        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                        .contentRetriever(contentRetriever)
                        .build();

                TokenStream tokenStream = ragService.ask(question);

                tokenStream
                        .onNext(s -> response.write(s))
                        .onComplete(aiMessageResponse -> response.end())
                        .onError(Throwable::printStackTrace)
                        .start();

            } else {
                System.out.println("..: Fazendo uma consulta com Natural Language Querie");

                Neo4JContentRetriever jContentRetriever = new Neo4JContentRetriever(model, Utils.getNeo4J());

                ResponseGenerationService responseGenerationService = AiServices.builder(ResponseGenerationService.class)
                        .streamingChatLanguageModel(getOpenAiStreamingChatModel())
                        .build();

                String cypherReturn = jContentRetriever.neo4jContentRetriever(question);

                System.out.println("..: Gerando resposta amigavel com a pergnta e respota abaixo:");
                System.out.println("..: Pergunta: " + question);
                System.out.println("..: Resposta: " + cypherReturn);

                TokenStream tokenStream = responseGenerationService.ask(question, cypherReturn);

                tokenStream
                        .onNext(s -> response.write(s))
                        .onComplete(aiMessageResponse -> response.end())
                        .onError(Throwable::printStackTrace)
                        .start();
            }
        });

    }

    private EmbeddingStore<TextSegment> getAiGeminiEmbeddingStire() {
        return Neo4jEmbeddingStore.builder()
                .driver(Utils.getNeo4JDrive())
                .dimension(768)
                .indexName("documentation")
                .embeddingProperty("embedding")
                .label("Microservice")
                .textProperty("content")
                .retrievalQuery("""
                        MATCH window=(:Microservice)-[:DEPEND_ON*0..1]->(node)-[:DEPEND_ON*0..1]->(:Microservice)
                        WITH window as longestWindow, node, score
                          ORDER BY length(window) DESC LIMIT 1
                        WITH nodes(longestWindow) as microServiceList, node, score
                          UNWIND microServiceList as microServiceRow
                        WITH collect(microServiceRow.content) as textList, node, score
                        RETURN properties(node) AS metadata, score, apoc.text.join(textList, " \\n ") as content, node.id AS id, node.embedding AS embedding
                        """)
                .awaitIndexTimeout(1000)
                .build();
    }

    private ChatLanguageModel getVertexAiGeminiChatModel() {
        return VertexAiGeminiChatModel.builder()
                .project(System.getenv("PROJECT_ID"))
                .location(System.getenv("LOCATION"))
                .modelName("gemini-1.5-flash-001")
                .maxOutputTokens(1000)
                .build();
    }

    private StreamingChatLanguageModel getAiGeminiStreamingChatModel() {
        return VertexAiGeminiStreamingChatModel.builder()
                .project(System.getenv("PROJECT_ID"))
                .location(System.getenv("LOCATION"))
                .modelName("gemini-1.5-flash-001")
                .maxOutputTokens(1000)
                .build();
    }

    private EmbeddingModel getAiGeminiEmbeddingModel() {
        return VertexAiEmbeddingModel.builder()
                .endpoint(System.getenv("LOCATION") + "-aiplatform.googleapis.com:443")
                .project(System.getenv("PROJECT_ID"))
                .location(System.getenv("LOCATION"))
                .publisher("google")
                .modelName("textembedding-gecko@003")
                .maxRetries(3)
                .build();
    }

    private StreamingChatLanguageModel getOpenAiStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-3.5-turbo")
                .build();
    }

    private ChatLanguageModel getOpenAiChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-3.5-turbo")
                .build();
    }

}
