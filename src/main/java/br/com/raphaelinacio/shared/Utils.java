package br.com.raphaelinacio.shared;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.store.graph.neo4j.Neo4jGraph;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

public class Utils {


  public static PathMatcher glob(String glob) {
    return FileSystems.getDefault().getPathMatcher("glob:" + glob);
  }

  public static Path toPath(String relativePath) {
    try {
      URL fileUrl = Utils.class.getClassLoader().getResource(relativePath);
      return Paths.get(fileUrl.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static Neo4jGraph getNeo4J() {
    return Neo4jGraph.builder().driver(getNeo4JDrive()).build();
  }

  public static Driver getNeo4JDrive() {
    return GraphDatabase.driver("bolt://localhost:7689", AuthTokens.basic("neo4j", "9bkqd7db"));
  }

  public static List<Document> getDocuments() {
    return loadDocuments(toPath("documents/"), glob("*.md"));
  }

  public static void main(String[] args) {

    VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
      .endpoint(System.getenv("LOCATION") + "-aiplatform.googleapis.com:443")
      .project(System.getenv("PROJECT_ID"))
      .location(System.getenv("LOCATION"))
      .publisher("google")
      .modelName("textembedding-gecko@003")
      .maxRetries(3)
      .build();

    for (Document document : getDocuments()) {
      Response<Embedding> response = embeddingModel.embed(document.toTextSegment());
      Embedding embedding = response.content();
      int dimension = embedding.dimension(); // 768
      float[] vector = embedding.vector(); // [-0.06050122, -0.046411075, ...
      System.out.println(document.metadata().getString("file_name"));
      System.out.println(embedding.vectorAsList());
    }


  }
}
